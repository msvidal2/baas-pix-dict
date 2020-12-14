package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "ListClaimsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListClaimsResponse {

    @XmlElement(name = "HasMoreElements")
    private Boolean hasMoreElements;

    @XmlElement(name = "Claims")
    private ClaimsList claimsList;

    public ClaimIterable toClaimIterable() {
        var claims = Collections.<Claim>emptyList();

        if(claimsList != null) {
             claims = claimsList.getClaims();
        }

        return ClaimIterable.builder()
                .hasNext(hasMoreElements)
                .count(claims.size())
                .claims(claims)
                .build();
    }

}
