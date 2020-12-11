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
import com.picpay.banking.pix.core.validators.infraction.WorkInfractionValidator;
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
    private final String ispbPicpay;

    public void execute(InfractionReport infractionReport) {

        WorkInfractionValidator.validateIspbLength(infractionReport);

        infractionReportSavePort.save(infractionReport);
        infractionNotificationPort.notify(infractionReport);

        if (makeAcknowledge(infractionReport))
            infractionAcknowledgePort.acknowledge(infractionReport.getInfractionReportId(), ispbPicpay);
    }

    private boolean makeAcknowledge(InfractionReport infractionReport) {
        if (infractionReport.getSituation().equals(InfractionReportSituation.OPEN)) {
            switch (infractionReport.getReportedBy()) {
                case DEBITED_PARTICIPANT:
                    if (!infractionReport.getIspbDebited().equalsIgnoreCase("22896431")) return true;
                    break;
                case CREDITED_PARTICIPANT:
                    if (!infractionReport.getIspbCredited().equalsIgnoreCase("22896431")) return true;
                    break;
            }
        }
        return false;
    }

}
