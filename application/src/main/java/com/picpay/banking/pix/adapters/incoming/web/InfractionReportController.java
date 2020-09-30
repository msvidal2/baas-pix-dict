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
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.ListPendingInfractionReportUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "InfractionReport")
@RestController
@RequestMapping(value = "/v1/infraction-report", produces = "application/json")
@AllArgsConstructor
public class InfractionReportController {

    private final ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;
    private final CreateInfractionReportUseCase createInfractionReportUseCase;
    private final CancelInfractionReportUseCase cancelInfractionReportUseCase;
    private final FindInfractionReportUseCase findInfractionReportUseCase;
    private final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;
    private final FilterInfractionReportUseCase filterInfractionReportUseCase;

    @Trace
    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(CREATED)
    public InfractionReportCreatedDTO report(@RequestBody @Valid CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        final InfractionReport infractionReport = createInfractionReportUseCase.execute(createInfractionReportRequestWebDTO.toInfractionReport(),
            createInfractionReportRequestWebDTO.getRequestIdentifier());
        return InfractionReportCreatedDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "List pendings infractions")
    @GetMapping(value = "/pendings/{ispb}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public List<InfractionReportDTO> listPending(@PathVariable("ispb") Integer ispb,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return this.listPendingInfractionReportUseCase.execute(ispb, limit).stream().map(InfractionReportDTO::from).collect(Collectors.toList());
    }

    @Trace
    @ApiOperation(value = "find an infraction report")
    @GetMapping(value = "/find/{infractionReportId}")
    @ResponseStatus(OK)
    public FindInfractionReportDTO find(@PathVariable String infractionReportId) {
        final InfractionReport infractionReport = findInfractionReportUseCase.execute(infractionReportId);
        return FindInfractionReportDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "Cancel Infraction Report")
    @PostMapping(value = "/{infractionReportId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public CancelResponseInfractionDTO cancel(@PathVariable("infractionReportId") String infractionReportId,
        @Valid @RequestBody CancelInfractionDTO dto) {
        var infractionReport = this.cancelInfractionReportUseCase.execute(infractionReportId, dto.getIspb(), dto.getRequestIdentifier());
        return CancelResponseInfractionDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "Analyze Infraction Report")
    @PostMapping(value = "/{infractionReportId}/analyze", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public CancelResponseInfractionDTO analyze(@PathVariable("infractionReportId") String infractionReportId,
        @Valid @RequestBody AnalyzeInfractionReportDTO dto) {
        var infractionReport = this.analyzeInfractionReportUseCase.execute(infractionReportId, dto.getIspb(), dto.toInfractionAnalyze(),
            dto.getRequestIdentifier());
        return CancelResponseInfractionDTO.from(infractionReport);
    }

    @Trace
    @ApiOperation(value = "List Infraction Report")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public List<InfractionReportDTO> filter(@Valid FilterInfractionReportDTO filter) {
        var listInfractionReport = this.filterInfractionReportUseCase.execute(
            filter.getIspb(), filter.getEhDebitado(), filter.getEhCreditado(), InfractionReportSituation.resolve(filter.getStRelatoInfracao()),
            filter.getDtHrModificacaoInicio(), filter.getDtHrModificacaoFim(), filter.getNrLimite());

        return listInfractionReport.stream().map(InfractionReportDTO::from).collect(Collectors.toList());
    }

}
