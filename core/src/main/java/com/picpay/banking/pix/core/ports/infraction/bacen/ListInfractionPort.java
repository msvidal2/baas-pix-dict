/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.ports.infraction.bacen;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;

import java.util.List;

public interface ListInfractionPort {

    List<InfractionReport> list();

}
