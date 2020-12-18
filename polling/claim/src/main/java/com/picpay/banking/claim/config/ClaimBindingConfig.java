/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBinding(value = {
        ClaimTopicBindingOutput.class
})
@Configuration
public class ClaimBindingConfig {

    @Bean
    public ClaimNotificationOutputBinding claimNotificationOutputBinding() {
        return () -> null;
    }

    @Bean
    public CancelPortabilityPollingOutputBinding cancelPortabilityPollingOutputBinding() {
        return () -> null;
    }

}
