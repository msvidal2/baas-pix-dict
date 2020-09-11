package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.response.PendingInfractionReportDTO;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        return infractionReportDTO.getInfractionReports().stream().map(PendingInfractionReportDTO::toInfraction)
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

}
