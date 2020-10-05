package com.picpay.banking.jdpi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticasResponseDTO {

    private LocalDateTime dtHrUltAtuAntiFraude;
    private Collection<EventoResponseDTO> contadores;
}
