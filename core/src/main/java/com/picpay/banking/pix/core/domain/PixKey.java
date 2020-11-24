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
@EqualsAndHashCode
public class PixKey {

    @EqualsAndHashCode.Include
    private KeyType type;

    @EqualsAndHashCode.Include
    private String key;

    @EqualsAndHashCode.Include
    private Integer ispb;

    private String nameIspb;

    @EqualsAndHashCode.Include
    private String branchNumber;

    @EqualsAndHashCode.Include
    private AccountType accountType;

    @EqualsAndHashCode.Include
    private String accountNumber;

    private LocalDateTime accountOpeningDate;

    @EqualsAndHashCode.Include
    private PersonType personType;

    @EqualsAndHashCode.Include
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
