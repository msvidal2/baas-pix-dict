package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class ContentIdentifier {

    private String cid;
    private String key;
    private KeyType keyType;

    private PixKey pixKey;
}
