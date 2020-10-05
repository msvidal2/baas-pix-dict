package com.picpay.banking.jdpi.dto.response;

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
public class StatisticResponseDTO {

    private LocalDateTime lastUpdateDateAntiFraud;
    private List<AccountantResponseDTO> accountants;

}
