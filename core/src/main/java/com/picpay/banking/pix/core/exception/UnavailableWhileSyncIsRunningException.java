/*
 *  baas-pix-dict 1.0 13/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.exception;

/**
 * @author rafael.braga
 * @version 1.0 13/01/2021
 */
public class UnavailableWhileSyncIsRunningException extends UseCaseException {

    private static final String MESSAGE = "DICT-API is currently syncing with BACEN and cannot serve requests. Please try again later";

    public UnavailableWhileSyncIsRunningException() {
        super(MESSAGE);
    }

}
