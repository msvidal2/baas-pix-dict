/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.ports.infraction.bacen;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
public interface CreateInfractionReportPort {

    @ValidateIdempotency(InfractionReport.class)
    InfractionReport create(InfractionReport infractionReport, @IdempotencyKey String requestIdentifier);

}
