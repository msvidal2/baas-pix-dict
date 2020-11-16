package com.picpay.banking.pixkey.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Account {

    private final String participant;
    private final String branch;
    private final String accountNumber;
    private final AccountType accountType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime openingDate;

    public static Account from(PixKey pixKey) {
        return Account.builder()
                .participant(String.valueOf(pixKey.getIspb()))
                .branch(pixKey.getBranchNumber())
                .accountNumber(pixKey.getBranchNumber())
                .accountType(AccountType.resolve(pixKey.getAccountType()))
                .openingDate(pixKey.getAccountOpeningDate())
                .build();
    }

}
