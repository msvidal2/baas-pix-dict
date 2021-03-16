/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.infraction.ports;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCacheSavePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 08/12/2020
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
public class InfractionReportCacheSavePortImpl implements InfractionReportCacheSavePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(@NonNull InfractionReportEventData infractionReportEventData, @NonNull String requestIdentifier) {
        redisTemplate.opsForHash().put(InfractionReportEventData.class.getName(), requestIdentifier, infractionReportEventData);
    }

}
