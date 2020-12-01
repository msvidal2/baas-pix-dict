package com.picpay.banking.jdpi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum JDErrorCode {

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
