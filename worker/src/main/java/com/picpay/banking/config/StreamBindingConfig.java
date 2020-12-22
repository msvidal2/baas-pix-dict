/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.config;

import com.picpay.banking.claim.config.*;
import com.picpay.banking.infraction.config.AcknowledgeInputStream;
import com.picpay.banking.infraction.config.AcknowledgeOutputStream;
import com.picpay.banking.infraction.config.InfractionAlertNotificationOutputStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 08/12/2020
 */
@EnableBinding(value = {
        AcknowledgeOutputStream.class,
        InfractionAlertNotificationOutputStream.class,
        AcknowledgeInputStream.class,
        ClaimNotificationInputBinding.class,
        ClaimNotificationOutputBinding.class,
        CancelPortabilityPollingInputBinding.class
})
@Configuration
public class StreamBindingConfig {

    @Bean
    public ClaimTopicBindingOutput claimTopicBindingOutput() {
        return () -> null;
    }

}
