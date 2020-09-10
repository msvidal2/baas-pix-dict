package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.Infraction;

public interface InfractionReportPort {

    Infraction execute(Infraction infraction, String requestIdentifier);

    Pagination<Infraction> listPendingInfractionReport(Integer ispb, Integer limit);

}
