package com.picpay.banking.jdpi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventoResponseDTO {

    private Integer tipo;
    private Integer agregado;
    private Integer d3;
    private Integer d30;
    private Integer m6;
}
