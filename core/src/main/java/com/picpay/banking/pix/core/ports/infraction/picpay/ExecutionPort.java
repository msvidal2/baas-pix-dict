package com.picpay.banking.pix.core.ports.infraction.picpay;/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.domain.ExecutionType;

public interface ExecutionPort {

    Execution lastExecution(ExecutionType executionType);
    void save(Execution execution);

}
