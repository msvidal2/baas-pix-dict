package com.picpay.banking.jdpi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JDErrorCode {

    // Relatos de Infração
    JDPIRIN001("RIN001", "A transação informada no Relato de Infração não foi encontrada."),
    JDPIRIN002("RIN002", "O prazo para o Relato de Infração sobre a transação expirou."),
    JDPIRIN003("RIN003", "Já existe um relato de infração fechado para a transação informada."),
    JDPIRIN004("RIN004", "Já existe um relato de infração em andamento para a transação informada."),
    JDPIRIN005("RIN005", "A situação do Relato de Infração não permite o seu cancelamento."),
    JDPIRIN006("RIN006", "O Relato de Infração só pode ser cancelado pelo PSP relator."),
    JDPIRIN007("RIN007", "O Relato de Infração não pode ser analisado pelo PSP relator.");

    private String code;

    private String message;

    public static JDErrorCode resolve(final String code) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(code))
                .findAny()
                .orElse(null);
    }

}
