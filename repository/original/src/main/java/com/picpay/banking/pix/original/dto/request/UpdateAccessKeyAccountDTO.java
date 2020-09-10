package com.picpay.banking.pix.original.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccessKeyAccountDTO {

    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private String accountType;
    private String branch;
    private String key;
    private String reason;

}
