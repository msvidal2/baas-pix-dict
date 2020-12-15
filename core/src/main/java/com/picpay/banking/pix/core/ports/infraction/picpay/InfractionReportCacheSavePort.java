package com.picpay.banking.pix.core.ports.infraction.picpay;/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.NonNull;

public interface InfractionReportCacheSavePort {

    void save(@NonNull InfractionReport infractionReport, @NonNull String requestIdentifier);

}
