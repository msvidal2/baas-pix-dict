package com.picpay.banking.infraction.ports.bacen;

import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.CancelInfractionReportPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 27/11/20
 */
@Component
@RequiredArgsConstructor
public class CancelInfractionReportPortImpl implements CancelInfractionReportPort {

    private static final String CIRCUIT_BREAKER_CREATE_NAME = "cancel-infraction";
    private final CreateInfractionBacenClient bacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    public InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier) {
        return null;
    }
}
