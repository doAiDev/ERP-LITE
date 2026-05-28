package com.erp.accessory.aop;

import com.erp.accessory.entity.AuditLog;
import com.erp.accessory.mapper.AuditMapper;
import com.erp.accessory.security.UserPrincipal;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditMapper auditMapper;

    @Around("execution(public * com.erp.accessory.service..*(..)) "
          + "&& (execution(* create*(..)) || execution(* insert*(..)) "
          + "|| execution(* update*(..)) || execution(* delete*(..)) "
          + "|| execution(* remove*(..)) || execution(* save*(..)))")
    public Object auditCudOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String action = determineAction(methodName);

        Object result = joinPoint.proceed();

        try {
            Integer staffId = null;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                staffId = principal.getStaffId();
            }

            String targetTable = className.replace("Service", "").toLowerCase();

            AuditLog auditLog = AuditLog.builder()
                .staffId(staffId)
                .action(action)
                .targetTable(targetTable)
                .targetId(extractTargetId(result))
                .build();

            auditMapper.insertAuditLog(auditLog);

        } catch (Exception e) {
            log.error("감사 로그 저장 실패: {}", e.getMessage());
        }

        return result;
    }

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

    private String extractTargetId(Object result) {
        if (result instanceof Integer) {
            return String.valueOf((Integer) result);
        } else if (result instanceof Long) {
            return String.valueOf((Long) result);
        }
        return null;
    }
}
