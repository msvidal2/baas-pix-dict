package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.InfractionReport;

public interface InfractionReportPort {

    InfractionReport create(InfractionReport infractionReport, String requestIdentifier);

}
