package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import lombok.*;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PixKey {

    private KeyType type;
    private String key;
    private Integer ispb;
    private String nameIspb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String taxId;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private String correlationId;
    private ClaimType claim;
    private Statistic statistic;

    public String getOwnerName() {
        if (PersonType.INDIVIDUAL_PERSON.equals(personType)) {
            return name;
        }

        return ObjectUtils.firstNonNull(fantasyName, name);
    }

    public String getTaxIdWithLeftZeros() {
        int size = 11;

        if(PersonType.LEGAL_ENTITY.equals(personType)) {
            size = 14;
        }

        return Strings.padStart(taxId, size, '0');
    }

}
