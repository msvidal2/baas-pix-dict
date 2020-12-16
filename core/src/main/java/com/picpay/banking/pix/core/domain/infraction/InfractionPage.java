package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InfractionPage {

    private long offset;
    private int pageSize;
    private long size;
    private List<InfractionReport> content;

}
