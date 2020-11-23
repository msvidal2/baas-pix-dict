package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import lombok.*;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PixKey pixKey = (PixKey) o;
        return type == pixKey.type &&
                key.equals(pixKey.key) &&
                ispb.equals(pixKey.ispb) &&
                branchNumber.equals(pixKey.branchNumber) &&
                accountType == pixKey.accountType &&
                accountNumber.equals(pixKey.accountNumber) &&
                personType == pixKey.personType &&
                taxId.equals(pixKey.taxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, ispb, branchNumber, accountType, accountNumber, personType, taxId);
    }

}
