package com.picpay.banking.jdpi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@Getter
@ToString
public class ListPixKeyResponseDTO {

    private LocalDateTime dtHrJdPi;
    private Collection<ListKeyResponseDTO> chavesAssociadas;
}
