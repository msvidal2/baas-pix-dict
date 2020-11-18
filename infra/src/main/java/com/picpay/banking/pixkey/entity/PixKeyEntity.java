/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.PersonType;
import com.picpay.banking.pixkey.dto.request.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
    private String branch;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private LocalDateTime openingDate;

    @Column(nullable = false)
    private PersonType personType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Reason reason;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime ownershipDate;

    private LocalDateTime updateDate;
    private LocalDateTime openClaimCreationDate;

    public PixKey toPixKey() {
        return PixKey.builder()
                .key(id.getKey())
                .type(id.getType().getType())
                .ispb(Integer.parseInt(participant))
                .branchNumber(branch)
                .accountType(accountType.getType())
                .accountNumber(accountNumber)
                .accountOpeningDate(openingDate)
                .personType(personType.getPersonType())
                .taxId(id.getTaxId())
                .name(name)
                .createdAt(creationDate)
                .startPossessionAt(ownershipDate)
                .endToEndId(correlationId)
                .build();
    }

}
