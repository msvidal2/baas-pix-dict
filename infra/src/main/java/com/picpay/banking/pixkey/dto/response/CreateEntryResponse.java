/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pixkey.dto.request.Entry;
import com.picpay.banking.pixkey.dto.request.Reason;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 16/11/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateEntryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateEntryResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "Entry")
    private Entry entry;

    public PixKeyEntity toEntity(final CreateReason createReason) {
        return PixKeyEntity.builder()
                .id(PixKeyIdEntity.builder()
                        .key(entry.getKey())
                        .type(entry.getKeyType())
                        .taxId(entry.getOwner().getTaxIdNumber())
                        .build())
                .participant(entry.getAccount().getParticipant())
                .branch(entry.getAccount().getBranch())
                .accountNumber(entry.getAccount().getAccountNumber())
                .accountType(entry.getAccount().getAccountType())
                .openingDate(entry.getAccount().getOpeningDate())
                .personType(entry.getOwner().getType())
                .name(entry.getOwner().getName())
                .reason(Reason.resolve(createReason))
                .correlationId(correlationId)
                .creationDate(entry.getCreationDate())
                .ownershipDate(entry.getKeyOwnershipDate())
                .build();
    }

}
