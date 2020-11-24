package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Vsync {

    private String vsync;
    private KeyType keyType;
    private LocalDateTime synchronizedAt;

}
