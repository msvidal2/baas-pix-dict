/*
 *  baas-pix-dict 1.0 11/25/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.ports.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;

/**
 * @author rafael.braga
 * @version 1.0 25/11/2020
 */
public interface AnalyzeInfractionReportPort {

    InfractionReport analyze(String infractionReportId, Integer ispb, InfractionAnalyze analyze, String requestIdentifier);

}
