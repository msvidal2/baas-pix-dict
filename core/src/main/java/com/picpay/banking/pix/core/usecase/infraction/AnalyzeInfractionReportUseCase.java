package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class AnalyzeInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId, @NonNull final Integer ispb, @NonNull InfractionAnalyze analyze, @NonNull final String requestIdentifier) {
        return infractionReportPort.analyze(infractionReportId,ispb, analyze, requestIdentifier);
    }

}
