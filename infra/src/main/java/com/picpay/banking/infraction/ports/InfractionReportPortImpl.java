/*
 *  baas-pix-dict 1.0 18/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.ports;

import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.dto.request.CreateInfractionReportRequest;
import com.picpay.banking.infraction.dto.response.CreateInfractionReportResponse;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 18/11/2020
 */
@RequiredArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

    private final CreateInfractionBacenClient bacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    public InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {
        CreateInfractionReportRequest requestDto = CreateInfractionReportRequest.from(infractionReport);
        CreateInfractionReportResponse responseDto = bacenClient.create(requestDto);
        return responseDto.toDomain();
    }

    @Override
    public InfractionReport find(final String infractionReportId, final String ispb) {
        return bacenClient.find(infractionReportId,ispb).toDomain();
    }

    @Override
    public List<InfractionReport> listPending(final Integer ispb, final Integer limit) {
        return null;
    }


    @Override
    public InfractionReport cancel(final String infractionReportId, final Integer ispb, final String requestIdentifier) {
        return null;
    }

    @Override
    public InfractionReport analyze(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
                                    final String requestIdentifier) {
        return null;
    }

    @Override
    public List<InfractionReport> list(final Integer isbp, final Boolean isDebited, final Boolean isCredited,
                                       final InfractionReportSituation situation,
                                       final LocalDateTime dateStart, final LocalDateTime dateEnd, final Integer limit) {
        return null;
    }

}
