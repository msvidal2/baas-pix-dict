/*
 *  baas-pix-dict 1.0 11/23/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.validators.idempotency;

import java.util.Optional;

public interface IdempotencyValidator<T> {

    Optional<T> validate(String idempotencyKey, T compareTo);

}
