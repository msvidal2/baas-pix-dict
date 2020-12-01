package com.picpay.banking.pix.adapters.incoming.web;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.adapters.incoming.web.dto.AnalyzeInfractionReportDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CancelInfractionDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CancelResponseInfractionDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FilterInfractionReportDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.FindInfractionReportDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportCreatedDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportDTO;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "InfractionReport")
@RestController
@RequestMapping(value = "/v1/infraction-report", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class InfractionReportController {

    private final FindInfractionReportUseCase findInfractionReportUseCase;
    private final CreateInfractionReportUseCase createInfractionReportUseCase;
    //    private final ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;
    //    private final CancelInfractionReportUseCase cancelInfractionReportUseCase;
    //    private final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;
    //    private final FilterInfractionReportUseCase filterInfractionReportUseCase;

    @Trace
    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(CREATED)
    public InfractionReportCreatedDTO report(@RequestHeader String requestIdentifier,
                                             @RequestBody @Valid CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        log.info("Infraction_reporting"
            , kv("requestIdentifier", requestIdentifier)
            , kv("endToEndId", createInfractionReportRequestWebDTO.getEndToEndId())
            , kv("infractionType", createInfractionReportRequestWebDTO.getInfractionType())
            , kv("iIspbRequester", createInfractionReportRequestWebDTO.getIspbRequester()));

        final var infractionReport = createInfractionReportUseCase.execute(CreateInfractionReportRequestWebDTO.from(createInfractionReportRequestWebDTO),
                                                                           requestIdentifier);

        return InfractionReportCreatedDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "find an infraction report")
    @GetMapping(value = "/{infractionReportId}")
    @ResponseStatus(OK)
    public FindInfractionReportDTO find(@PathVariable String infractionReportId, @Valid @PathParam("ispb") Integer ispb) {
        log.info("Infraction_finding", kv("infractionReportId", infractionReportId));

        final InfractionReport infractionReport = findInfractionReportUseCase.execute(infractionReportId);

        return FindInfractionReportDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "Cancel Infraction Report")
    @PostMapping(value = "/{infractionReportId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public CancelResponseInfractionDTO cancel(@RequestHeader String requestIdentifier
        , @PathVariable("infractionReportId") String infractionReportId, @Valid @RequestBody CancelInfractionDTO dto) {

//        log.info("Infraction_canceling"
//            , kv("requestIdentifier", requestIdentifier)
//            , kv("infractionReportId", infractionReportId)
//            , kv("infractionType", dto.getIspb()));
//
//        var infractionReport = this.cancelInfractionReportUseCase
//            .execute(infractionReportId, dto.getIspb(), requestIdentifier);
//
//        return CancelResponseInfractionDTO.from(infractionReport);

        throw new UnsupportedOperationException("Não implementado");
    }

    @Trace
    @ApiOperation(value = "Analyze Infraction Report")
    @PostMapping(value = "/{infractionReportId}/analyze", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public CancelResponseInfractionDTO analyze(@RequestHeader String requestIdentifier
        , @PathVariable("infractionReportId") String infractionReportId
        , @Valid @RequestBody AnalyzeInfractionReportDTO dto) {

//        log.info("Infraction_analyzing"
//            , kv("requestIdentifier", requestIdentifier)
//            , kv("infractionReportId", infractionReportId)
//            , kv("infractionType", dto.getIspb()));
//
//        var infractionReport = this.analyzeInfractionReportUseCase
//            .execute(infractionReportId, dto.getIspb(), dto.toInfractionAnalyze(), requestIdentifier);
//
//        return CancelResponseInfractionDTO.from(infractionReport);

        throw new UnsupportedOperationException("Não implementado");
    }

    @Trace
    @ApiOperation(value = "List Infraction Report")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public List<InfractionReportDTO> filter(@Valid FilterInfractionReportDTO filter) {
//        log.info("Infraction_filtering", kv("requestIdentifier", filter.getIspb()));
//
//        var listInfractionReport = this.filterInfractionReportUseCase.execute(
//            filter.getIspb(), filter.getEhDebitado(), filter.getEhCreditado(),
//            InfractionReportSituation.resolve(filter.getStRelatoInfracao()),
//            filter.getDtHrModificacaoInicio(), filter.getDtHrModificacaoFim(), filter.getNrLimite());
//
//        return listInfractionReport.stream().map(InfractionReportDTO::from).collect(Collectors.toList());

        throw new UnsupportedOperationException("Não implementado");
    }

}
