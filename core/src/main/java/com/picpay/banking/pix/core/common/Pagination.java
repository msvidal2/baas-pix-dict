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

    private Integer totalRecords;
    private Integer currentPage;
    private Integer pageSize;
    private Boolean hasPrevious;
    private Boolean hasNext;
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

}
