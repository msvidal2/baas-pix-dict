package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "DeleteEntryRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveEntryRequest {

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "Participant")
    private String participant;

    @XmlElement(name = "Reason")
    private Reason reason;

    public static RemoveEntryRequest from(PixKey pixKey, RemoveReason reason) {
        return RemoveEntryRequest.builder()
                .key(pixKey.getKey())
                .participant(Integer.toString(pixKey.getIspb()))
                .reason(Reason.resolve(reason))
                .build();
    }

}
