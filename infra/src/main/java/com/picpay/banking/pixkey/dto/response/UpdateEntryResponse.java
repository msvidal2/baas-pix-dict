package com.picpay.banking.pixkey.dto.response;

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

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "UpdateEntryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateEntryResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "Entry")
    private Entry entry;

    public PixKey toDomain(PixKey pixKey) {
        return PixKey.builder()
                .type(KeyType.resolve(entry.getKeyType().getValue()))
                .key(entry.getKey())
                .ispb(pixKey.getIspb())
                .branchNumber(entry.getAccount().getBranch())
                .accountType(AccountType.resolve(entry.getAccount().getAccountType().getValue()))
                .accountNumber(entry.getAccount().getAccountNumber())
                .accountOpeningDate(entry.getAccount().getOpeningDate())
                .personType(PersonType.resolve(entry.getOwner().getType().getValue()))
                .taxId(entry.getOwner().getTaxIdNumber())
                .name(entry.getOwner().getName())
                .fantasyName(entry.getOwner().getName())
                .createdAt(entry.getCreationDate())
                .startPossessionAt(entry.getKeyOwnershipDate())
                .correlationId(correlationId)
                .build();
    }
}
