/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.ports.execution;

import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.domain.ExecutionType;

import java.util.Optional;

public interface ExecutionPort {

    Optional<Execution> lastExecution(ExecutionType executionType);
    void save(Execution execution);

}
