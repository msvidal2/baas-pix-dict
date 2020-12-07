/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationSenderPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@RequiredArgsConstructor
public class InfractionPollingUseCase {

    private final InfractionNotificationSenderPort senderPort;
    private final ListInfractionPort listInfractionPort;

    public void execute(final Integer ispb, final Integer limit) {
        List<InfractionReport> infractions = listInfractionPort.list();
        infractions.forEach(senderPort::send);
    }

}
