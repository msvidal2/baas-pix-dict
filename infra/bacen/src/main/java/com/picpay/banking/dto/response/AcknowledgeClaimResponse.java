package com.picpay.banking.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "AcknowledgeClaimResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcknowledgeClaimResponse {

    @XmlElement(name = "Claim")
    private ClaimResponse claim;

    public Claim toClaim() {
        return claim.toClaim();
    }

}
