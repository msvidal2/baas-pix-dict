package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Entry {

    private String key;
    private KeyType keyType;
    private Account brazilianAccount;
    private Owner owner;

    public static Entry from(PixKey pixKey) {
        return Entry.builder()
                .key(pixKey.getKey())
                .keyType(KeyType.resolve(pixKey.getType()))
                .brazilianAccount(Account.from(pixKey))
                .owner(Owner.from(pixKey))
                .build();
    }

}
