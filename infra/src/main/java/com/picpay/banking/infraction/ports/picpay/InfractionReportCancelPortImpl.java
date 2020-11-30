package com.picpay.banking.infraction.ports.picpay;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
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

    private InfractionReportRepository infractionReportRepository;

    @Override
    public InfractionReport cancel(String infractionReportId) {
        var infraEntity = infractionReportRepository
                .changeSituation(infractionReportId, InfractionReportSituation.CANCELED.getValue());

        return infraEntity.ifPresent(infractionReportEntity -> infractionReportEntity.toDomain());
    }
}
