package com.epicmed.developer.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

    private Integer page;
    private Integer size;
    private Integer totalItems;
    private Integer totalPages;
    private List<T> data;

    public boolean hasNext() {
        return page != null && totalPages != null && page < totalPages;
    }

    public boolean hasPrevious() {
        return page != null && page > 1;
    }

    public static <T> PageResponseDto<T> empty() {
        return PageResponseDto.<T>builder()
                .page(0)
                .size(0)
                .totalItems(0)
                .totalPages(0)
                .data(List.of())
                .build();
    }

    public static <T> PageResponseDto<T> of(List<T> data, int page, int size, int totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return PageResponseDto.<T>builder()
                .page(page)
                .size(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .data(data)
                .build();
    }
}
