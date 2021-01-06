package com.picpay.banking.pix.core.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {

    private Long totalRecords;
    @Builder.Default
    private Integer currentPage = 0;
    private Integer pageSize;
    @Builder.Default
    private Boolean hasPrevious = false;
    @Builder.Default
    private Boolean hasNext = false;
    private List<T> result;

    public <R> Pagination<R> copy(Function<T,R> function){
        List<R> list = result.stream().map(function).collect(Collectors.toList());
        Pagination<R> pagination = new Pagination<>();
        pagination.setResult(list);
        pagination.setTotalRecords(totalRecords);
        pagination.setCurrentPage(currentPage);
        pagination.setHasPrevious(hasPrevious);
        pagination.setPageSize(pageSize);
        pagination.setHasNext(hasNext);
        return pagination;
    }

    public Integer nextPage() {
        return this.currentPage == null ? 0 : (this.currentPage + 1);
    }

}
