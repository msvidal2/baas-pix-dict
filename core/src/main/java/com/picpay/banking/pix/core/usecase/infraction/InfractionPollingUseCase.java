/*
 *  baas-pix-dict 1.0 12/7/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.ListInfractionReports;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ExecutionType.INFRACTION_POLLING;
import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 07/12/2020
 */
@RequiredArgsConstructor
@Slf4j
public class InfractionPollingUseCase {

    private final SendToAcknowledgePort acknowledgePort;
    private final ListInfractionPort listInfractionPort;
    private final ExecutionPort executionPort;
    private static final LocalDateTime FIRST_DAY_OF_PIX = LocalDateTime.of(2020, 11, 16, 0, 0, 0, 0);
    private static final String SUCCESS = "SUCCESS";

    public void execute(final String ispb, final Integer limit) {
        LocalDateTime startTime = LocalDateTime.now();
        try {
            poll(ispb, limit);
            executionPort.save(Execution.success(startTime, LocalDateTime.now(), INFRACTION_POLLING));
        } catch (Exception e) {
            log.error("Infraction Polling failed: ", e);
            executionPort.save(Execution.fail(startTime, LocalDateTime.now(), INFRACTION_POLLING, e));
        }
    }

    private void poll(final String ispb, final Integer limit) {
        log.info("Polling BACEN for infractions");
        Execution execution = executionPort.lastExecution(INFRACTION_POLLING).orElseGet(this::defaultExecution);
        List<InfractionReport> infractions = pollWhile(execution, ispb, limit);
        log.info("Infraction_list_received -> size: {}", kv("infraction_list_size", infractions.size()));
        infractions.forEach(acknowledgePort::send);
    }

    private List<InfractionReport> pollWhile(Execution execution, final String ispb, final Integer limit) {
        var startDate = execution.getEndTime();
        var endDate = LocalDateTime.now(ZoneId.of("UTC"));

        log.info("Start: {}, End: {}", startDate, endDate);

        ListInfractionReports infractions;
        List<InfractionReport> infractionReports = new ArrayList<>();

        do {
            infractions = listInfractionPort.list(ispb, limit, startDate, endDate);

            if (!CollectionUtils.isEmpty(infractions.getInfractionReports())) {
                infractionReports.addAll(infractions.getInfractionReports());
            }

            if (infractions.isHasMoreElements() && infractions.getSize() > 0) {
                startDate = infractions.getInfractionReports().get(infractions.getSize() - 1).getDateLastUpdate().plusSeconds(1);
            }
        } while (infractions.isHasMoreElements());

        return infractionReports;
    }

    private Execution defaultExecution() {
        return Execution.builder()
            .endTime(FIRST_DAY_OF_PIX)
            .exitMessage(SUCCESS)
            .build();
    }

}
