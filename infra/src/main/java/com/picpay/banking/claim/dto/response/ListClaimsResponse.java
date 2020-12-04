package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<ClaimResponse> claims;

    public ClaimIterable toClaimIterable() {
        return ClaimIterable.builder()
                .hasNext(hasMoreElements)
                .count(claims.size())
                .claims(getClaims())
                .build();
    }

    private List<Claim> getClaims() {
        return claims.stream()
                .map(ClaimResponse::toClaim)
                .collect(Collectors.toList());
    }

}
