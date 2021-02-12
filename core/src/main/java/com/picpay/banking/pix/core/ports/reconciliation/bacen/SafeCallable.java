/*
 *  baas-pix-dict 1.0 11/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.ports.reconciliation.bacen;

/**
 * @author rafael.braga
 * @version 1.0 11/02/2021
 */
@FunctionalInterface
public interface SafeCallable<V> extends java.util.concurrent.Callable<V> {

    V call();

}