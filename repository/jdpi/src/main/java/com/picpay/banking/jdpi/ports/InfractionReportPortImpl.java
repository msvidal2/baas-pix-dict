package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.AnalyzeInfractionReportDTO;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.request.FilterInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.InfractionReportDTO;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

    private final InfractionReportJDClient infractionJDClient;

    @Override
    public InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {

        return infractionJDClient.create(CreateInfractionReportRequestDTO.from(infractionReport), requestIdentifier)
            .toInfractionReport();
    }

    @Override
    public List<InfractionReport> listPendingInfractionReport(final Integer ispb, final Integer limit) {
        var infractionReportDTO = this.infractionJDClient.listPendings(ispb, limit);
        return infractionReportDTO.getInfractionReports().stream().map(InfractionReportDTO::toInfraction)
            .collect(Collectors.toList());
    }

    @Override
    public InfractionReport find(final String infractionReportId) {
        return infractionJDClient.find(infractionReportId).toInfractionReport();
    }

    @Override
    public InfractionReport cancel(final String infractionReportId, final Integer ispb, final String requestIdentifier) {
        var cancelInfractionDTO = new CancelInfractionDTO(infractionReportId, ispb);
        var response = this.infractionJDClient.cancel(cancelInfractionDTO, requestIdentifier);
        return response.toInfraction();
    }

    @Override
    public InfractionReport analyze(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
        final String requestIdentifier) {

        var analyzeInfractionReport = AnalyzeInfractionReportDTO.builder()
            .analyzeDetails(analyze.getDetails())
            .analyzeResult(analyze.getAnalyzeResult().getValue())
            .ispb(ispb)
            .infractionReportId(infractionReportId)
            .build();

        var analyzeResponse = this.infractionJDClient.analyze(analyzeInfractionReport, requestIdentifier);

        return analyzeResponse.toInfraction();
    }

    @Override
    public List<InfractionReport> filter(Integer isbp, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation,
        LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit) {

        var filter = FilterInfractionReportDTO.builder()
            .ispb(isbp)
            .ehDebitado(isDebited)
            .ehCreditado(isCredited)
            .stRelatoInfracao(situation == null ? null : situation.getValue())
            .dtHrModificacaoInicio(dateStart)
            .dtHrModificacaoFim(dateEnd)
            .incluiDetalhes(true)
            .nrLimite(limit)
        .build();

        var listInfractionReportDTO = this.infractionJDClient.filter(filter);

        return listInfractionReportDTO.getInfractionReports().stream().map(InfractionReportDTO::toInfraction).collect(Collectors.toList());
    }

}
