/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@RequiredArgsConstructor
@Slf4j
public class InfractionPollingUseCase {

    private final SendToAcknowledgePort acknowledgePort;
    private final ListInfractionPort listInfractionPort;

    public void execute(final Integer ispb, final Integer limit) {
        List<InfractionReport> infractions = listInfractionPort.list(ispb, limit);

        if (infractions != null) {
            log.info("Infraction_list_received -> size: {}"
                , kv("infraction_list_size", infractions.size()));
            infractions.forEach(acknowledgePort::send);
        }
    }

}
