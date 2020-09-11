package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;

import java.util.List;

public interface InfractionReportPort {

    InfractionReport create(InfractionReport infractionReport, String requestIdentifier);

    List<InfractionReport> listPendingInfractionReport(Integer ispb, Integer limit);

    InfractionReport find(String infractionReportId);

    InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier);

    InfractionReport analyze(String infractionReportId, Integer ispb, InfractionAnalyze analyze, String requestIdentifier);

}
