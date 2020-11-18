package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.adapters.LocalDateTimeAdapter;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Entry {

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "KeyType")
    private KeyType keyType;

    @XmlElement(name = "Account")
    private Account account;

    @XmlElement(name = "Owner")
    private Owner owner;

    @XmlElement(name = "CreationDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationDate;

    @XmlElement(name = "KeyOwnershipDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime keyOwnershipDate;

    public static Entry from(PixKey pixKey) {
        return Entry.builder()
                .key(pixKey.getKey())
                .keyType(KeyType.resolve(pixKey.getType()))
                .account(Account.from(pixKey))
                .owner(Owner.from(pixKey))
                .build();
    }

    public PixKeyEntity toEntity() {
        return PixKeyEntity.builder()
                .id(PixKeyIdEntity.builder()
                        .key(key)
                        .type(keyType)
                        .taxId(owner.getTaxIdNumber())
                        .build())

                .build();
    }

}
