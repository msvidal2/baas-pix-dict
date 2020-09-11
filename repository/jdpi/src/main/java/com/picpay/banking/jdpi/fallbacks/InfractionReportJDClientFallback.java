package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.response.CancelResponseInfractionDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPendingInfractionReportDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfractionReportJDClientFallback extends JDClientFallback implements InfractionReportJDClient {

    public InfractionReportJDClientFallback(Throwable cause) {
        super(cause);
    }


    @Override
    public CreateInfractionReportResponseDTO create(final CreateInfractionReportRequestDTO request, final String requestIdentifier) {
        throw resolveException();
    }

    @Override
    public ListPendingInfractionReportDTO listPendings(final Integer ispb, final Integer nrLimite) {
        throw resolveException();
    }

    @Override
    public FindInfractionReportResponseDTO find(final String infractionReportId) {
        throw resolveException();
    }

    @Override
    public CancelResponseInfractionDTO cancel(final CancelInfractionDTO cancelInfractionDTO, final String requestIdentifier) {
        throw resolveException();
    }

}
