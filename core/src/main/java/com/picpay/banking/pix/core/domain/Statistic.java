package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class Statistic {

    private LocalDateTime lastUpdateDateAntiFraud;
    private List<Accountant> accountants;

}
