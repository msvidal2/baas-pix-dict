/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.infraction.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface AcknowledgeOutputStream {

    @Output("new-infractions")
    MessageChannel sendInfractionForAcknowledgment();

}
