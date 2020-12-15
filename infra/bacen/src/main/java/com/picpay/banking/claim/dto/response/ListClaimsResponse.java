package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @XmlElementWrapper(name = "Claims")
    @XmlElement(name = "Claim")
    private List<ClaimResponse> claims;

    public ClaimIterable toClaimIterable() {
        var listClaims = Optional.ofNullable(claims)
                .orElse(Collections.emptyList())
                .stream()
                .map(ClaimResponse::toClaim)
                .collect(Collectors.toList());

        return ClaimIterable.builder()
                .hasNext(hasMoreElements)
                .count(listClaims.size())
                .claims(listClaims)
                .build();
    }

}
