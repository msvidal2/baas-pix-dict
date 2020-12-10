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
@XmlRootElement(name = "ConfirmClaimRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfirmClaimRequest {

    @XmlElement(name = "ClaimId")
    private String claimId;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "Reason")
    private ConfirmClaimReason reason;

    public static ConfirmClaimRequest from(Claim claim) {
        return ConfirmClaimRequest.builder()
                .claimId(claim.getClaimId())
                .participant(String.valueOf(claim.getIspb()))
                .reason(ConfirmClaimReason.resolve(claim.getConfirmationReason()))
                .build();
    }

}
