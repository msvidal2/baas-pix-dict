package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Owner {

    @XmlElement(name = "Type")
    private OwnerType type;

    @XmlElement(name = "TaxIdNumber")
    private String taxIdNumber;

    @XmlElement(name = "Name")
    private String name;

    public static Owner from(PixKey pixKey) {
        return Owner.builder()
                .type(OwnerType.resolve(pixKey.getPersonType()))
                .taxIdNumber(pixKey.getTaxId())
                .name(pixKey.getOwnerName())
                .build();
    }

}