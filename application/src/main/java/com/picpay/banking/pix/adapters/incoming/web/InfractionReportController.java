package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportCreatedDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportDTO;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.ListPendingInfractionReportUseCase;
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

    private ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;
    private CreateInfractionReportUseCase infractionReportUseCase;

    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(CREATED)
    public InfractionReportCreatedDTO report(@RequestBody @Valid CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        final InfractionReport infractionReport = infractionReportUseCase.execute(createInfractionReportRequestWebDTO.toInfractionReport(), createInfractionReportRequestWebDTO.getRequestIdentifier());
        return InfractionReportCreatedDTO.from(infractionReport);
    }


    @ApiOperation(value = "List pendings infractions")
    @GetMapping(value = "/pendings/{ispb}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public List<InfractionReportDTO> listPending(@PathVariable("ispb") Integer ispb, @RequestParam(value = "limit",defaultValue = "10") Integer limit) {
       return this.listPendingInfractionReportUseCase.execute(ispb, limit).stream().map(InfractionReportDTO::from).collect(Collectors.toList());
    }

}
