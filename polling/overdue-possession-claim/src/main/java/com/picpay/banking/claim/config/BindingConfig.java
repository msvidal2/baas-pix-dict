/*
 *  baas-pix-dict 1.0 22/12/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.claim.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 22/12/2020
 */
@EnableBinding(value = {OverduePossessionClaimDonorOutputTopic.class,
                        OverduePossessionClaimClaimerOutputTopic.class})
@Configuration
public class BindingConfig {

}
