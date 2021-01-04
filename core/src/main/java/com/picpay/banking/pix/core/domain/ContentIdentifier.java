package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ContentIdentifier {

    private String cid;
    private String key;
    private KeyType keyType;
    private LocalDateTime keyOwnershipDate;
    private PixKey pixKey;

}
