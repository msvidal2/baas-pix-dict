package com.picpay.banking.pix.core.ports.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;

public interface InfractionReportCancelPort {

    InfractionReport cancel(String infractionReportId);

}
