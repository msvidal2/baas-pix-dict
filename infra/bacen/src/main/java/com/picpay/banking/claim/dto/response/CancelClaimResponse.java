package com.picpay.banking.claim.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CancelClaimResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancelClaimResponse {

    @XmlElement(name = "ResponseTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime responseTime;

    @XmlElement(name = "CorrelationId")
    private String correlationId;

    @XmlElement(name = "Claim")
    private ClaimResponse claim;

    public Claim toClaim() {
        return claim.toClaim(correlationId);
    }

}
