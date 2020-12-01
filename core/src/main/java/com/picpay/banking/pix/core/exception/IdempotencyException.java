/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.exception;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
public class IdempotencyException extends UseCaseException {

    private static final String MESSAGE = "Uma entidade com esse request identifier com body diferente já foi criada anteriormente";

    public IdempotencyException() {
        super(MESSAGE);
    }

}
