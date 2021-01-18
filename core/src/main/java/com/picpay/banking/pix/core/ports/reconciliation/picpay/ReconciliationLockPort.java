/*
 *  baas-pix-dict 1.0 14/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.ports.reconciliation.picpay;

public interface ReconciliationLockPort {

    void lock();
    void unlock();

}
