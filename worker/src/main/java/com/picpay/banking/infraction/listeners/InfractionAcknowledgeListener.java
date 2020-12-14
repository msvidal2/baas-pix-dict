/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.infraction.listeners;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.usecase.infraction.InfractionAcknowledgeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionAcknowledgeListener {

    private final InfractionAcknowledgeUseCase infractionAcknowledgeUseCase;

    @StreamListener("new-infraction-reports")
    public void listen(final InfractionReport infractionReport) {
        infractionAcknowledgeUseCase.execute(infractionReport);
    }

}
