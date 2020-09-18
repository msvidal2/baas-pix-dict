package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimerAccountDTO {

    private long accountNumber;
    private AccountTypeOriginal accountType;
    private String branch;
    private int participant;

}
