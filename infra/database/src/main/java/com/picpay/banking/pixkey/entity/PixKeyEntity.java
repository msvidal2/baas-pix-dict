/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.UpdateReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity(name = "pix_key")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PixKeyEntity {

    @EmbeddedId
    private PixKeyIdEntity id;

    @Column(nullable = false)
    private Integer participant;

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
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime ownershipDate;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime openClaimCreationDate;

    @Column
    private String taxId;

    @Column
    private String requestId;

    @Column
    private String cid;

    public static PixKeyEntity from(final PixKey pixKey, final CreateReason reason) {
        return PixKeyEntity.builder()
            .id(PixKeyIdEntity.builder()
                .key(pixKey.getKey())
                .type(pixKey.getType())
                .build())
            .requestId(pixKey.getRequestId().toString())
            .taxId(pixKey.getTaxId())
            .participant(pixKey.getIspb())
            .branch(pixKey.getBranchNumber())
            .accountNumber(pixKey.getAccountNumber())
            .accountType(pixKey.getAccountType())
            .openingDate(pixKey.getAccountOpeningDate())
            .personType(pixKey.getPersonType())
            .name(pixKey.getName())
            .reason(reason.getValue())
            .correlationId(pixKey.getCorrelationId())
            .creationDate(pixKey.getCreatedAt())
            .ownershipDate(pixKey.getStartPossessionAt())
            .requestId(pixKey.getRequestId().toString())
            .cid(pixKey.getCid())
            .build();
    }

    public static PixKeyEntity from(final PixKey pixKey, final UpdateReason reason) {
        return PixKeyEntity.builder()
            .id(PixKeyIdEntity.builder()
                .key(pixKey.getKey())
                .type(pixKey.getType())
                .build())
            .taxId(pixKey.getTaxId())
            .requestId(pixKey.getRequestId().toString())
            .participant(pixKey.getIspb())
            .branch(pixKey.getBranchNumber())
            .accountNumber(pixKey.getAccountNumber())
            .accountType(pixKey.getAccountType())
            .openingDate(pixKey.getAccountOpeningDate())
            .personType(pixKey.getPersonType())
            .name(pixKey.getName())
            .reason(Reason.resolve(reason.getValue()))
            .correlationId(pixKey.getCorrelationId())
            .creationDate(pixKey.getCreatedAt())
            .ownershipDate(pixKey.getStartPossessionAt())
            .requestId(pixKey.getRequestId().toString())
            .cid(pixKey.getCid())
            .build();
    }

    public PixKey toPixKey() {
        return PixKey.builder()
            .type(com.picpay.banking.pix.core.domain.KeyType.resolve(id.getType().getValue()))
            .key(id.getKey())
            .ispb(participant)
            .branchNumber(branch)
            .accountType(accountType)
            .accountNumber(accountNumber)
            .accountOpeningDate(openingDate)
            .personType(personType)
            .taxId(taxId)
            .name(name)
            .createdAt(creationDate)
            .startPossessionAt(ownershipDate)
            .correlationId(correlationId)
            .requestId(UUID.fromString(requestId))
            .cid(cid)
            //TODO incluir claim?
            //                .claim()
            .build();
    }

}
