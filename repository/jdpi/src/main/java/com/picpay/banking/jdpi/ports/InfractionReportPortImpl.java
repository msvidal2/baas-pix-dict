package com.picpay.banking.jdpi.ports;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.Infraction;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

    @Override
    public Infraction execute(Infraction infraction, String requestIdentifier) {

        return null;
    }

    @Override
    public Pagination<Infraction> listPendingInfractionReport(final Integer ispb, final Integer limit) {
        return null;
    }

}
