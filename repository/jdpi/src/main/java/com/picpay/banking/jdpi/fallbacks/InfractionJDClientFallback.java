package com.picpay.banking.jdpi.fallbacks;

import com.picpay.banking.jdpi.clients.InfractionJDClient;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfractionJDClientFallback extends JDClientFallback implements InfractionJDClient {

    public InfractionJDClientFallback(Throwable cause) {
        super(cause);
    }


    @Override
    public CreateInfractionReportResponseDTO create(final CreateInfractionReportRequestDTO request, final String requestIdentifier) {
        throw resolveException();
    }

}
