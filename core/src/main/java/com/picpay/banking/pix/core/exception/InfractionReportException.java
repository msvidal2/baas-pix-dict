/*
 *  baas-pix-dict 1.0 11/24/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.exception;

import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 24/11/2020
 */
@Getter
public class InfractionReportException extends UseCaseException {

    private final InfractionReportError infractionReportError;

    public InfractionReportException(final InfractionReportError infractionReportError) {
        this.infractionReportError = infractionReportError;
    }

    public InfractionReportException(final String message, final InfractionReportError infractionReportError) {
        super(message);
        this.infractionReportError = infractionReportError;
    }

}
