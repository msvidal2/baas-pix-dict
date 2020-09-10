package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportCreatedDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportDTO;
import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.usecase.ListPendingInfractionReportUseCase;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "InfractionReport")
@RestController
@RequestMapping(value = "/v1/infraction", produces = "application/json")
@AllArgsConstructor
public class InfractionReportController {

    private ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;
    private CreateInfractionReportUseCase infractionReportUseCase;

    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(CREATED)
    public InfractionReportCreatedDTO report(@RequestBody @Validated CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        final InfractionReport infractionReport = infractionReportUseCase.execute(createInfractionReportRequestWebDTO.toInfraction(), createInfractionReportRequestWebDTO.getRequestIdentifier());
        return InfractionReportCreatedDTO.from(infractionReport);
    }


    @ApiOperation(value = "List pendings infractions")
    @GetMapping(value = "/pendings/{ispb}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Pagination<InfractionReportDTO> listPending(@PathVariable("ispb") Integer ispb, @RequestParam(value = "limit",defaultValue = "10") Integer limit) {
        Pagination<InfractionReport> pagination = this.listPendingInfractionReportUseCase.execute(ispb, limit);
        return pagination.copy(InfractionReportDTO::from);
    }

}
