package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.ClaimType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class ListKeyResponseWebDTO {

    private String key;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime startPossessionAt;
    private int claim;

}
