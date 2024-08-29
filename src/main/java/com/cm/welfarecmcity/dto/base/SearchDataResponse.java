package com.cm.welfarecmcity.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDataResponse<T> {

    private List<T> contents;

    private Long totalPages;

    private Long totalElements;

    public static <T> SearchDataResponse<T> fromData(
            List<T> contents, Long totalElements, Long pageSize) {
        Long totalPages = (long) Math.ceil((double) totalElements / pageSize);
        SearchDataResponse<T> searchDataDto = new SearchDataResponse<>();
        searchDataDto.setContents(contents);
        searchDataDto.setTotalPages(totalPages);
        searchDataDto.setTotalElements(totalElements);
        return searchDataDto;
    }
}
