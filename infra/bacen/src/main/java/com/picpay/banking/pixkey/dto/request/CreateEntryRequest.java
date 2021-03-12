package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "CreateEntryRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateEntryRequest {

    @XmlElement(name = "Entry")
    private Entry entry;

    @XmlElement(name = "Reason")
    private ReasonDTO reason;

    @XmlElement(name = "RequestId")
    private String requestId;

    public static CreateEntryRequest from(PixKey pixKey, com.picpay.banking.pix.core.domain.Reason reason, String requestId) {
        return CreateEntryRequest.builder()
                .entry(Entry.from(pixKey))
                .reason(ReasonDTO.resolve(reason))
                .requestId(requestId)
                .build();
    }

}
