/*
 *  baas-pix-dict 1.0 11/24/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.exception;

/**
 * @author rafael.braga
 * @version 1.0 24/11/2020
 */
public class DuplicatedInfractionReportException extends RuntimeException {

    private static final String MESSAGE = "Uma infração com request-identifier %s já foi criada anteriormente";

    public DuplicatedInfractionReportException(final String idempotencyKey) {
        super(String.format(MESSAGE, idempotencyKey));
    }

}
