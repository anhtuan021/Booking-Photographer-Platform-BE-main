package org.bookingplatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    private List<T> content;
    private PaginationInfo pagination;
    
    @Data
    public static class PaginationInfo {
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean hasNext;
        private Boolean hasPrevious;
        
        public PaginationInfo(Integer page, Integer size, Long totalElements) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.hasNext = page < totalPages;
            this.hasPrevious = page > 1;
        }
    }
    
    public PaginationResponse(List<T> content, Integer page, Integer size, Long totalElements) {
        this.content = content;
        this.pagination = new PaginationInfo(page, size, totalElements);
    }
}
