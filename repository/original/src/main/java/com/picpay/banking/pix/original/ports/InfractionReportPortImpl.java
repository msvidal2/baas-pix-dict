package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.InfractionReportPort;

import java.time.LocalDateTime;
import java.util.List;

public class InfractionReportPortImpl  implements InfractionReportPort {
    @Override
    public InfractionReport create(InfractionReport infractionReport, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<InfractionReport> listPendingInfractionReport(Integer ispb, Integer limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InfractionReport find(String infractionReportId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InfractionReport analyze(String infractionReportId, Integer ispb, InfractionAnalyze analyze, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<InfractionReport> filter(Integer isbp, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation, LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit) {
        throw new UnsupportedOperationException();
    }
}
