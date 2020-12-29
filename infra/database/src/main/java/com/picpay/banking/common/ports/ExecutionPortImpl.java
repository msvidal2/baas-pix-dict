/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.common.ports;

import com.picpay.banking.common.entity.ExecutionEntity;
import com.picpay.banking.common.repository.ExecutionRepository;
import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.domain.ExecutionType;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author rafael.braga
 * @version 1.0 11/12/2020
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExecutionPortImpl implements ExecutionPort {

    private static final String SUCCESS = "SUCCESS";
    private final ExecutionRepository executionRepository;

    @Override
    public Optional<Execution> lastExecution(ExecutionType executionType) {
        return executionRepository.findFirstByExitMessageAndTaskNameOrderByEndTimeDesc(SUCCESS, executionType.toString()).map(ExecutionEntity::to);
    }

}
