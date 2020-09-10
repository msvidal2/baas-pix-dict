package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
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

}
