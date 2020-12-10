/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionAcknowledgePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import lombok.RequiredArgsConstructor;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@RequiredArgsConstructor
public class InfractionAcknowledgeUseCase {

    private final InfractionReportSavePort infractionReportSavePort;
    private final InfractionNotificationPort infractionNotificationPort;
    private final InfractionAcknowledgePort infractionAcknowledgePort;

    public void execute(InfractionReport infractionReport) {

        infractionReportSavePort.save(infractionReport);
        infractionNotificationPort.notify(infractionReport);

        if (infractionReport.getSituation().equals(InfractionReportSituation.OPEN))
            infractionAcknowledgePort.acknowledge(infractionReport.getInfractionReportId()
                    , Integer.toString(infractionReport.getIspbRequester()));

    }

}
