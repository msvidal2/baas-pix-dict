/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.ports.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;

import java.time.LocalDateTime;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
public interface InfractionReportAnalyzePort {

   InfractionReport analyze(final String infractionReportId, final Integer ispb,
       InfractionAnalyze analyze, final LocalDateTime dateLastUpdate, final String requestIdentifier);

}
