package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.repository.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportCancelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 30/11/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InfractionReportCancelPortImpl implements InfractionReportCancelPort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public void cancel(String infractionReportId) {
        infractionReportRepository.changeSituation(infractionReportId, InfractionReportSituation.CANCELED.getValue());
    }

}
