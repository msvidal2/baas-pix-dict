package com.picpay.banking.jdpi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreatePixKeyResponseDTO {

    protected String key;
    protected LocalDateTime createdAt;
    protected LocalDateTime startPossessionAt;
}
