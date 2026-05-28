package com.erp.accessory.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 페이지네이션 응답 DTO
 * - content: 조회 결과 목록
 * - totalCount: 전체 데이터 수
 * - totalPages: 전체 페이지 수
 * - currentPage: 현재 페이지
 * - pageSize: 페이지당 항목 수
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;    // 조회 결과 목록
    private int totalCount;     // 전체 데이터 수
    private int totalPages;     // 전체 페이지 수
    private int currentPage;    // 현재 페이지
    private int pageSize;       // 페이지당 항목 수
    private boolean hasNext;    // 다음 페이지 존재 여부
    private boolean hasPrevious; // 이전 페이지 존재 여부

    /**
     * PageRequest와 전체 텟수를 받아 PageResponse 생성
     */
    public static <T> PageResponse<T> of(List<T> content, int totalCount, PageRequest pageRequest) {
        int totalPages = (int) Math.ceil((double) totalCount / pageRequest.getSize());
        if (totalPages == 0) totalPages = 1;

        return PageResponse.<T>builder()
            .content(content)
            .totalCount(totalCount)
            .totalPages(totalPages)
            .currentPage(pageRequest.getPage())
            .pageSize(pageRequest.getSize())
            .hasNext(pageRequest.getPage() < totalPages)
            .hasPrevious(pageRequest.getPage() > 1)
            .build();
    }
}
