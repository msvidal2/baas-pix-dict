/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.infraction.repository.InfractionReportRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionPage;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author marcelo.vidal
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportListPortImpl implements InfractionReportListPort {

    private final InfractionReportRepository infractionReportRepository;

    @Override
    public InfractionPage list(final InfractionReportSituation situation,
                               final LocalDateTime dateStart,
                               final LocalDateTime dateEnd,
                               final int page,
                               final int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<InfractionReportEntity> pageInfraction = infractionReportRepository.list(situation, dateStart, dateEnd, pageRequest);
        List<InfractionReport> content = pageInfraction.getContent().stream().map(InfractionReportEntity::toDomain).collect(Collectors.toList());

        return InfractionPage.builder()
            .content(content)
            .size(pageInfraction.getTotalElements())
            .offset(pageInfraction.getPageable().getOffset())
            .pageSize(pageInfraction.getPageable().getPageSize())
            .build();
    }

}
