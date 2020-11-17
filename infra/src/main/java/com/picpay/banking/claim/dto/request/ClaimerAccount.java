package com.picpay.banking.claim.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.picpay.banking.pixkey.dto.request.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ClaimerAccount {

    private final String participant;
    private final String branch;
    private final String accountNumber;
    private final AccountType accountType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime openingDate;

}
