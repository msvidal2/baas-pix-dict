/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports.picpay;

import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@Component
@RequiredArgsConstructor
public class InfractionReportSavePortImpl implements InfractionReportSavePort {

    private static final String HASH_NAME = "INFRACTION";
    private final InfractionReportRepository infractionReportRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(@NonNull final InfractionReport infractionReport, final @NonNull String requestIdentifier) {
        infractionReportRepository.save(InfractionReportEntity.fromDomain(infractionReport));
        redisTemplate.opsForHash().put(HASH_NAME, requestIdentifier, infractionReport);
    }

}
