package com.picpay.banking.dto.response;

import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ClaimsList {

    @XmlElement(name = "Claim")
    private List<ClaimResponse> claims;

    public List<Claim> getClaims() {
        return claims.stream()
                .map(ClaimResponse::toClaim)
                .collect(Collectors.toList());
    }
}
