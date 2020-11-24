package com.picpay.banking.pix.core.ports.infraction;


import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;

import java.time.LocalDateTime;
import java.util.List;

public interface InfractionReportPort {

    InfractionReport create(InfractionReport infractionReport, String requestIdentifier);

    InfractionReport find(final String infractionReportId, final String ispb);

    InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier);

    InfractionReport analyze(String infractionReportId, Integer ispb, InfractionAnalyze analyze, String requestIdentifier);

    List<InfractionReport> list(Integer isbp, InfractionReportSituation situation, LocalDateTime dateStart, LocalDateTime dateEnd);

}
