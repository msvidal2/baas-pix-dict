package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.InfractionReport;

public interface InfractionReportPort {

    InfractionReport create(InfractionReport infractionReport, String requestIdentifier);

    Pagination<InfractionReport> listPendingInfractionReport(Integer ispb, Integer limit);

}
