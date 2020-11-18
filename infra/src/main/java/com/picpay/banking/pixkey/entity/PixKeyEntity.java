/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.dto.request.PersonType;
import com.picpay.banking.pixkey.dto.request.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "pix_key")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PixKeyEntity {

    @EmbeddedId
    private PixKeyIdEntity id;

    @Column(nullable = false)
    private String participant;

    @Column(nullable = false)
    private String nameParticipant;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private LocalDateTime openingDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fantasyName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime ownershipDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime openClaimCreationDate;

    public static PixKeyEntity from(final PixKey pixKey, final CreateReason reason) {
        return PixKeyEntity.builder()
                .id(PixKeyIdEntity.builder()
                        .key(pixKey.getKey())
                        .type(KeyType.resolve(pixKey.getType()))
                        .taxId(pixKey.getTaxId())
                        .build())
                .participant(String.valueOf(pixKey.getIspb()))
                .nameParticipant(pixKey.getNameIspb())
                .branch(pixKey.getBranchNumber())
                .accountNumber(pixKey.getAccountNumber())
                .accountType(AccountType.resolve(pixKey.getAccountType()))
                .openingDate(pixKey.getAccountOpeningDate())
                .personType(PersonType.resolve(pixKey.getPersonType()))
                .name(pixKey.getName())
                .fantasyName(pixKey.getFantasyName())
                .reason(Reason.resolve(reason))
                .correlationId(pixKey.getCorrelationId())
                .creationDate(pixKey.getCreatedAt())
                .ownershipDate(pixKey.getStartPossessionAt())
                .build();
    }

    public PixKey toPixKey() {
        return PixKey.builder()
                .type(com.picpay.banking.pix.core.domain.KeyType.resolve(id.getType().getValue()))
                .key(id.getKey())
                .ispb(Integer.parseInt(participant))
                .nameIspb(nameParticipant)
                .branchNumber(branch)
                .accountType(com.picpay.banking.pix.core.domain.AccountType.resolve(accountType.getValue()))
                .accountNumber(accountNumber)
                .accountOpeningDate(openingDate)
                .personType(com.picpay.banking.pix.core.domain.PersonType.resolve(personType.getValue()))
                .taxId(id.getTaxId())
                .name(name)
                .fantasyName(fantasyName)
                .createdAt(creationDate)
                .startPossessionAt(ownershipDate)
                //TODO Implementar atributo endToEndId
                .endToEndId("NOT IMPLEMENTED")
                .correlationId(correlationId)
                //TODO Implementar atributo claim
//                .claim()
                .build();
    }

}
