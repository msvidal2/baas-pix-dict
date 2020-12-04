package com.picpay.banking.pix.core.ports.infraction.bacen;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;

public interface CancelInfractionReportPort {

    InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier);

}
