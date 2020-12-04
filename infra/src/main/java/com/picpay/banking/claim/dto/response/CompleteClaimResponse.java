package com.picpay.banking.claim.dto.response;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateClaimResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompleteClaimResponse {

    @XmlElement(name = "Claim")
    private ClaimResponse claim;

    @XmlElement(name = "EntryCreationDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime entryCreationDate;

    @XmlElement(name = "KeyOwnershipDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime keyOwnershipDate;

    public Claim toClaim() {
        return claim.toClaim();
    }
}
