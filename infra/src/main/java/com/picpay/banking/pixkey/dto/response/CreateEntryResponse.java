/*
 *  baas-pix-dict 1.0 16/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pixkey.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.Entry;
import com.picpay.banking.pixkey.dto.request.Reason;
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
import java.util.UUID;

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

    public PixKey toDomain(String requestIdentifier, Reason resolve) {
        return PixKey.builder()
                .key(entry.getKey())
                .type(entry.getKeyType().getType())
                .ispb(Integer.parseInt(entry.getAccount().getParticipant()))
                .branchNumber(entry.getAccount().getBranch())
                .accountType(entry.getAccount().getAccountType().getType())
                .accountNumber(entry.getAccount().getAccountNumber())
                .accountOpeningDate(entry.getAccount().getOpeningDate())
                .personType(entry.getOwner().getType().getPersonType())
                .taxId(entry.getOwner().getTaxIdNumber())
                .name(entry.getOwner().getName())
                .createdAt(entry.getCreationDate())
                .startPossessionAt(entry.getKeyOwnershipDate())
                .correlationId(correlationId)
                .requestId(UUID.fromString(requestIdentifier))
                .build();
    }

}
