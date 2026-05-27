package com.erp.accessory.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이지네이션 요청 DTO
 * - page: 페이지 번호 (1부터 시작)
 * - size: 페이지당 조회 수
 * - offset: MSSQL OFFSET ... FETCH NEXT 에 사용
 */
@Getter
@Setter
public class PageRequest {

    private int page = 1;   // 현재 페이지 (1 기반)
    private int size = 20;  // 페이지당 조회 수

    public PageRequest() {}

    public PageRequest(int page, int size) {
        this.page = Math.max(1, page);     // 친소 1페이지
        this.size = Math.max(1, Math.min(size, 100)); // 1~100 사이
    }

    /**
     * MSSQL OFFSET 값 계산
     * OFFSET #{offset} ROWS FETCH NEXT #{size} ROWS ONLY
     */
    public int getOffset() {
        return (page - 1) * size;
    }

    /**
     * 유효한 page 값 보장
     */
    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    /**
     * 유효한 size 값 보장
     */
    public void setSize(int size) {
        this.size = Math.max(1, Math.min(size, 100));
    }
}
