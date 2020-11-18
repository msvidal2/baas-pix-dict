package com.picpay.banking.pix.core.ports.infraction;


import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;

import java.time.LocalDateTime;
import java.util.List;

public interface InfractionReportPort {

    InfractionReport create(InfractionReport infractionReport, String requestIdentifier);

    List<InfractionReport> listPending(Integer ispb, Integer limit);

    InfractionReport find(final String infractionReportId, final Integer ispb);

    InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier);

    InfractionReport analyze(String infractionReportId, Integer ispb, InfractionAnalyze analyze, String requestIdentifier);

    List<InfractionReport> list(Integer isbp, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation,
                                LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit);

}
