package com.picpay.banking.dto.request;

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
@XmlRootElement(name = "CompleteClaimRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompleteClaimRequest {

    @XmlElement(name = "ClaimId")
    private String claimId;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "RequestId")
    private String requestId;

    public static CompleteClaimRequest from(Claim claim, String requestIdentifier) {
        return CompleteClaimRequest.builder()
                .claimId(claim.getClaimId())
                .participant(String.valueOf(claim.getIspb()))
                .requestId(requestIdentifier)
                .build();
    }
}
