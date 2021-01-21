package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class Statistic implements Serializable {

    private static final long serialVersionUID = 8858424361329006903L;

    private LocalDateTime lastUpdateDateAntiFraud;
    private List<Accountant> accountants;

}
