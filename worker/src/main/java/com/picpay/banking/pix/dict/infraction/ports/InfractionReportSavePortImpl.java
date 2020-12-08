package com.picpay.banking.pix.dict.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.infraction.repository.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 08/12/20
 */
@RequiredArgsConstructor
@Component
public class InfractionReportSavePortImpl implements InfractionReportSavePort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    @Deprecated
    public void save(@NonNull InfractionReport infractionReport, @NonNull String requestIdentifier) {
       throw new IllegalCallerException("unused method for this domain.");
    }

    @Override
    public void save(@NonNull InfractionReport infractionReport) {
        var entity = InfractionReportEntity.fromDomain(infractionReport);
        infractionReportRepository.save(entity);
    }
}
