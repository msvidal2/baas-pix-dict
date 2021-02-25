/*
 *  baas-pix-dict 1.0 11/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

/**
 * @author rafael.braga
 * @version 1.0 11/02/2021
 */
@Getter
@AllArgsConstructor
public class ResultCidFile {

    private final Set<String> cids;

    public static ResultCidFile emptyCidFile() {
        return new ResultCidFile(Collections.emptySet());
    }

}
