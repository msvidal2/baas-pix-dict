/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.infraction.ports.picpay.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportListPortImpl implements InfractionReportListPort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public List<InfractionReport> list(final Integer ispb, final InfractionReportSituation situation, final LocalDateTime dateStart,
        final LocalDateTime dateEnd) {

        var result = infractionReportRepository.list(ispb,situation, dateStart, dateEnd);

        if(result.isEmpty()){
            return Collections.EMPTY_LIST;
        }

        var test = result.stream().map(InfractionReportEntity::toDomain).collect(Collectors.toList());
        return test;
    }

}
