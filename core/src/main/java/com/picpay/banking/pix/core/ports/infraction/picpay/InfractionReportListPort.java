/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.ports.infraction.picpay;


import com.picpay.banking.pix.core.domain.infraction.InfractionPage;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
public interface InfractionReportListPort {

   InfractionPage list(Integer ispb, InfractionReportSituation situation, LocalDateTime dateStart, LocalDateTime dateEnd, int page, int size);

}
