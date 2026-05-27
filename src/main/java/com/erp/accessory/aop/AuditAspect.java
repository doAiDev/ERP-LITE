package com.erp.accessory.aop;

import com.erp.accessory.entity.AuditLog;
import com.erp.accessory.mapper.AuditMapper;
import com.erp.accessory.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 감사 로그 AOP
 * - @Around로 서비스 레이어의 CUD(Create/Update/Delete) 메서드 실행 감지
 * - audit_logs 테이블에 자동 기록
 * - 메서드명 패턴: create*, insert*, update*, delete*, remove*, save*
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditMapper auditMapper;

    /**
     * 서비스 레이어의 CUD 메서드 감지
     * - com.erp.accessory.service 패키지 하위의 모든 public 메서드 대상
     * - 메서드명이 create/insert/update/delete/remove/save로 시작하는 경우만 기록
     */
    @Around("execution(public * com.erp.accessory.service..*(..))"
          + " && (execution(* create*(..)) || execution(* insert*(..)) "
          + "|| execution(* update*(..)) || execution(* delete*(..)) "
          + "|| execution(* remove*(..)) || execution(* save*(..)))")
    public Object auditCudOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 메서드 실행 전 정보 수집
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 2. 애스펝터에서 action 결정
        String action = determineAction(methodName);

        // 3. 메서드 실행
        Object result = joinPoint.proceed();

        // 4. 감사 로그 데이터 수집
        try {
            Integer userId = null;
            Integer staffId = null;

            // SecurityContext에서 현재 사용자 정보 추출
            Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                    && authentication.getPrincipal() instanceof UserPrincipal principal) {
                userId = principal.getUserId();
                staffId = principal.getStaffId();
            }

            // HTTP 요청에서 IP 및 User-Agent 추출
            String ipAddress = null;
            String userAgent = null;
            ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                ipAddress = getClientIp(request);
                userAgent = request.getHeader("User-Agent");
            }

            // 대상 테이블 추정 (ClassName에서 Service 제거)
            String targetTable = className.replace("Service", "").toLowerCase();

            // 감사 로그 저장
            AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .staffId(staffId)
                .action(action)
                .targetTable(targetTable)
                .targetId(extractTargetId(result))
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

            auditMapper.insertAuditLog(auditLog);

            log.debug("감사 로그 기록 - action: {}, table: {}, userId: {}",
                action, targetTable, userId);

        } catch (Exception e) {
            // 감사 로그 실패는 실제 로직에 영향을 주면 안 됨
            log.error("감사 로그 저장 실패: {}", e.getMessage());
        }

        return result;
    }

    /**
     * 메서드명에서 action 구분
     */
    private String determineAction(String methodName) {
        if (methodName.startsWith("create") || methodName.startsWith("insert")
                || methodName.startsWith("save")) {
            return "CREATE";
        } else if (methodName.startsWith("update")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DELETE";
        }
        return "UNKNOWN";
    }

    /**
     * 간단한 IP 추출 (Proxy 고려)
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For는 여러 IP가 있을 수 있음
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 반환값에서 대상 ID 추출
     * - Integer나 Long 반환 시 ID로 해석
     */
    private String extractTargetId(Object result) {
        if (result instanceof Integer id) {
            return String.valueOf(id);
        } else if (result instanceof Long id) {
            return String.valueOf(id);
        }
        return null;
    }
}
