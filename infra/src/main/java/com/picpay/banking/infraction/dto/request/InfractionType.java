/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum InfractionType {

    FRAUD(0),
    AML_CTF(1);

    private final int value;

    public static InfractionType from(com.picpay.banking.pix.core.domain.infraction.InfractionType origin) {
        return Stream.of(values())
            .filter(infractionType ->  origin.getValue() == infractionType.value)
            .findFirst()
            .orElse(FRAUD);
    }

}
