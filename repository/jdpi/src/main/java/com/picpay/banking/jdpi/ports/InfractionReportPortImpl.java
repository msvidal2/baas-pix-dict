package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

    InfractionReportJDClient infractionJDClient;

    @Override
    public InfractionReport create(InfractionReport infractionReport, String requestIdentifier) {

        return infractionJDClient.create(CreateInfractionReportRequestDTO.from(infractionReport), requestIdentifier)
            .toInfractionReport();
    }

    @Override
    public Pagination<InfractionReport> listPendingInfractionReport(final Integer ispb, final Integer limit) {
        ListPendingInfractionReportDTO infractionReportDTO = this.infractionJDClient.listPendings(ispb, limit);
        List<InfractionReport> infractionReports = infractionReportDTO.getReporteInfracao().stream().map(PendingInfractionReportDTO::toInfraction)
            .collect(Collectors.toList());
        return PaginationFactory.create(infractionReports,infractionReportDTO.getTemMaisElementos());
    }

}
