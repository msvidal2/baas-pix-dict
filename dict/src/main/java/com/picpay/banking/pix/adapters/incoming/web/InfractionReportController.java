package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.*;
import com.picpay.banking.pix.core.domain.infraction.InfractionPage;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import com.picpay.banking.pix.core.usecase.infraction.*;
import com.picpay.banking.pix.core.validators.reconciliation.lock.UnavailableWhileSyncIsActive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import static com.picpay.banking.pix.core.domain.InfractionReportEvent.*;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "InfractionReport")
@RestController
@RequestMapping(value = "/v1/infraction-report", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@UnavailableWhileSyncIsActive
public class InfractionReportController {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    public static final String INFRACTION_TYPE = "infractionType";
    public static final String INFRACTION_REPORT_ID = "infractionReportId";

    private final FindInfractionReportUseCase findInfractionReportUseCase;
    private final FilterInfractionReportUseCase filterInfractionReportUseCase;
    private final InfractionEventRegistryUseCase infractionEventRegistryUseCase;

    @Trace
    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public InfractionReportCreatedDTO report(@RequestHeader String requestIdentifier,
                                             @RequestBody @Valid CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        log.info("Infraction_reporting",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("endToEndId", createInfractionReportRequestWebDTO.getEndToEndId()),
                kv(INFRACTION_TYPE, createInfractionReportRequestWebDTO.getInfractionType()),
                kv("ispbRequester", createInfractionReportRequestWebDTO.getIspbRequester()));

        final var infractionReport = CreateInfractionReportRequestWebDTO.from(createInfractionReportRequestWebDTO);

        infractionEventRegistryUseCase.execute(PENDING_CREATE, requestIdentifier, infractionReport);

        return InfractionReportCreatedDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "find an infraction report")
    @GetMapping(value = "/{infractionReportId}")
    @ResponseStatus(OK)
    public FindInfractionReportDTO find(@PathVariable String infractionReportId, @Valid @PathParam("ispb") Integer ispb) {
        log.info("Infraction_finding", kv(INFRACTION_REPORT_ID, infractionReportId));

        final InfractionReport infractionReport = findInfractionReportUseCase.execute(infractionReportId);

        return FindInfractionReportDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "Cancel Infraction Report")
    @PostMapping(value = "/{infractionReportId}/cancel")
    @ResponseStatus(ACCEPTED)
    public CancelResponseInfractionDTO cancel(@RequestHeader String requestIdentifier,
                                              @PathVariable(INFRACTION_REPORT_ID) String infractionReportId,
                                              @Valid @RequestBody CancelInfractionDTO dto) {

        log.info("Infraction_canceling",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(INFRACTION_REPORT_ID, infractionReportId),
                kv(INFRACTION_TYPE, dto.getIspb()));

        var eventData = InfractionReportEventData.builder()
                .infractionReportId(infractionReportId)
                .ispb(dto.getIspb())
                .build();

        infractionEventRegistryUseCase.execute(PENDING_CANCEL, requestIdentifier, eventData);

        return CancelResponseInfractionDTO.builder()
                .infractionReportId(infractionReportId)
                .build();
    }

    @Trace
    @ApiOperation(value = "Analyze Infraction Report")
    @PostMapping(value = "/{infractionReportId}/analyze")
    @ResponseStatus(ACCEPTED)
    public CancelResponseInfractionDTO analyze(@RequestHeader String requestIdentifier,
                                               @PathVariable(INFRACTION_REPORT_ID) String infractionReportId,
                                               @Valid @RequestBody AnalyzeInfractionReportDTO dto) {
        log.info("Infraction_analyzing",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv(INFRACTION_REPORT_ID, infractionReportId),
                kv(INFRACTION_TYPE, dto.getIspb()));

        var eventData = InfractionReportEventData.builder()
                .infractionReportId(infractionReportId)
                .analyze(dto.toInfractionAnalyze())
                .build();

        infractionEventRegistryUseCase.execute(PENDING_ANALYZE, requestIdentifier, eventData);

        return CancelResponseInfractionDTO.builder()
                .infractionReportId(infractionReportId)
                .build();
    }

    @Trace
    @ApiOperation(value = "List Infraction Report")
    @GetMapping
    @ResponseStatus(OK)
    public InfractionPage filter(@Valid FilterInfractionReportDTO filter,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        log.info("Infraction_filtering", kv(REQUEST_IDENTIFIER, filter.getIspb()));
        return this.filterInfractionReportUseCase.execute(filter.getSituation(),
                                                          filter.getStartDateAsLocalDateTime(),
                                                          filter.getEndDateAsLocalDateTime(),
                                                          page,
                                                          size);
    }

}
