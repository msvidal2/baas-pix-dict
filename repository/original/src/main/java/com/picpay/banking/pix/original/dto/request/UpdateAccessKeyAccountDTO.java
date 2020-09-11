package com.picpay.banking.pix.original.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.ReasonOriginal;
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
    private AccountTypeOriginal accountType;
    private String branch;
    private String key;
    private ReasonOriginal reason;

    public static UpdateAccessKeyAccountDTO from(PixKey pixKey, UpdateReason reason) {
        return UpdateAccessKeyAccountDTO.builder()
                .key(pixKey.getKey())
                .accountType(AccountTypeOriginal.resolveFromDomain(pixKey.getAccountType()))
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(pixKey.getAccountOpeningDate())
                .reason(ReasonOriginal.resolve(reason.getValue()))
                .build();
    }

}
