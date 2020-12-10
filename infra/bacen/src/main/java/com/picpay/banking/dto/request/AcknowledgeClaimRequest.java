package com.picpay.banking.dto.request;

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
@XmlRootElement(name = "AcknowledgeClaimRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcknowledgeClaimRequest {

    @XmlElement(name = "ClaimId")
    private String claimId;

    @XmlElement(name = "Participant")
    private String participant;

}
