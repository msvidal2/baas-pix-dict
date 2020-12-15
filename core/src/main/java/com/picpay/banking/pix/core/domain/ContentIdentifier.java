package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class ContentIdentifier {

    private String cid;
    private String key;
    private KeyType keyType;

    private PixKey pixKey;

}
