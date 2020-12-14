package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

@ExtendWith(MockitoExtension.class)
class InfractionPollingUseCaseTest {

    @Mock
    private SendToAcknowledgePort sendToAcknowledgePort;
    @Mock
    private ListInfractionPort listInfractionPort;
    @InjectMocks
    private InfractionPollingUseCase infractionPollingUseCase;

    @Test
    void execute_success() {
    }

}