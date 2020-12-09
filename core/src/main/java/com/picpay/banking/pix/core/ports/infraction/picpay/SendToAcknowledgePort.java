/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.ports.infraction.picpay;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.NonNull;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
public interface SendToAcknowledgePort {

    void send(@NonNull final InfractionReport infractionReport);

}
