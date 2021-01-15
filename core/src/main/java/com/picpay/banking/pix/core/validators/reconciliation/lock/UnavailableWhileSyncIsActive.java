/*
 *  baas-pix-dict 1.0 12/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.validators.reconciliation.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks a controller to check if the DICT synchronization is running. If it's executing,
 * then all DICT operations are suspended until the sync is finished.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnavailableWhileSyncIsActive {

}
