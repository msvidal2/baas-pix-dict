package com.picpay.banking.claim.dto.request;

import com.picpay.banking.claim.dto.ClaimReason;
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
@XmlRootElement(name = "CancelClaimRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancelClaimRequest {

    @XmlElement(name = "ClaimId")
    private String claimId;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "Reason")
    private ClaimReason reason;

}
