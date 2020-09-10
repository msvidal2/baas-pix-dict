package com.picpay.banking.pix.core.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
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

}
