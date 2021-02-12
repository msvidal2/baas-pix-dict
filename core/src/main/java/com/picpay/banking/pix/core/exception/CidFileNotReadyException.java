/*
 *  baas-pix-dict 1.0 12/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.exception;

/**
 * @author rafael.braga
 * @version 1.0 12/02/2021
 */
public class CidFileNotReadyException extends UseCaseException {

    private static final String MESSAGE = "O arquivo de CIDs %s não está disponível no BACEN";

    public CidFileNotReadyException(Integer contentIdentifier) {
        super(String.format(MESSAGE, contentIdentifier));
    }

}
