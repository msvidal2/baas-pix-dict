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
public class CreateAccessKeyDTO {

    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private String accountType;
    private String branch;
    private String businessPerson;
    private String key;
    private String keyType;
    private String name;
    private String reason;
    private String status;
    private String taxId;
    private String typePerson;

}
