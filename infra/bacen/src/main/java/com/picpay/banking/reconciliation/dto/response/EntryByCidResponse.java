package com.picpay.banking.reconciliation.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.dto.request.Entry;
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
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "GetEntryByCidResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryByCidResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "Cid")
    private String cid;

    @XmlElement(name = "RequestId")
    private String requestId;

    @XmlElement(name = "Entry")
    private Entry entry;

    public PixKey toDomain() {
        // TODO: Aparentemente o campo ownerTradeName é utilizado no cid para CNPJ e não esta participando da classe PixKey
        return PixKey.builder()
            .type(KeyType.resolve(entry.getKeyType().getValue()))
            .key(entry.getKey())
            .ispb(Integer.parseInt(entry.getAccount().getParticipant()))
            .branchNumber(entry.getAccount().getBranch())
            .accountType(AccountType.resolve(entry.getAccount().getAccountType().getValue()))
            .accountNumber(entry.getAccount().getAccountNumber())
            .accountOpeningDate(entry.getAccount().getOpeningDate())
            .personType(PersonType.resolve(entry.getOwner().getType().getValue()))
            .taxId(entry.getOwner().getTaxIdNumber())
            .name(entry.getOwner().getName())
            .createdAt(entry.getCreationDate())
            .updatedAt(responseTime)
            .startPossessionAt(entry.getKeyOwnershipDate())
            .correlationId(correlationId)
            .requestId(UUID.fromString(requestId))
            .cid(cid)
        .build();
    }

}
