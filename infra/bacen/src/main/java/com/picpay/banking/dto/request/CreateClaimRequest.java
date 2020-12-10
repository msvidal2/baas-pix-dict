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
@XmlRootElement(name = "CreateClaimRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateClaimRequest {

    @XmlElement(name = "Claim")
    private ClaimRequest claim;

    public static CreateClaimRequest from(com.picpay.banking.pix.core.domain.Claim claim) {
        return CreateClaimRequest.builder()
                .claim(ClaimRequest.from(claim))
                .build();
    }

}