package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
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
public class CreateClaimResponse {

    @XmlElement(name = "Claim")
    private ClaimResponse claim;

    public Claim toClaim() {
        return claim.toClaim();
    }

}