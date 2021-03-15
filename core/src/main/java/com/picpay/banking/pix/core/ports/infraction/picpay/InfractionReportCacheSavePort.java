package com.picpay.banking.pix.core.ports.infraction.picpay;/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import lombok.NonNull;

public interface InfractionReportCacheSavePort {

    void save(@NonNull InfractionReportEventData infractionReportEventData, @NonNull String requestIdentifier);

}
