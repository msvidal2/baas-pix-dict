package com.picpay.banking.jdpi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ListClaimResponseDTO {

    private LocalDateTime dtHrJdPi;
    private Boolean temMaisElementos;
    private Collection<ListClaimDTO> reivindicacoesAssociadas;
}
