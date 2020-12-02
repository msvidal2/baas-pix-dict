package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pixkey.dto.request.Account;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.pixkey.dto.request.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimRequest {

    @XmlElement(name = "Type")
    private ClaimType type;

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "KeyType")
    private KeyTypeBacen keyType;

    @XmlElement(name = "ClaimerAccount")
    private Account claimerAccount;

    @XmlElement(name = "Claimer")
    private Owner claimer;

    public static ClaimRequest from(Claim claim) {
        return ClaimRequest.builder()
                .type(ClaimType.resolve(claim.getClaimType()))
                .key(claim.getKey())
                .keyType(KeyTypeBacen.resolve(claim.getKeyType()))
                .claimerAccount(Account.from(claim))
                .claimer(Owner.from(claim))
                .build();
    }

}
