package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
@XmlRootElement(name = "UpdateEntryRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateEntryRequest {

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "Account")
    private Account account;

    @XmlElement(name = "Reason")
    private ReasonDTO reason;

    public static UpdateEntryRequest from(PixKey pixKey, Reason reason) {
        return UpdateEntryRequest.builder()
                .key(pixKey.getKey())
                .account(Account.from(pixKey))
                .reason(ReasonDTO.resolve(reason))
                .build();
    }
}
