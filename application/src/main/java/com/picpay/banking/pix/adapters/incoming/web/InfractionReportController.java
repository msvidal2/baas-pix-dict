package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportCreatedDTO;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Api(value = "InfractionReport")
@RestController
@RequestMapping(value = "/v1/infraction-report", produces = "application/json")
@AllArgsConstructor
public class InfractionReportController {

    private CreateInfractionReportUseCase infractionReportUseCase;

    @ApiOperation(value = "Create a new infraction report")
    @PostMapping
    @ResponseStatus(CREATED)
    public InfractionReportCreatedDTO report(@RequestBody @Valid CreateInfractionReportRequestWebDTO createInfractionReportRequestWebDTO) {
        final InfractionReport infractionReport = infractionReportUseCase.execute(createInfractionReportRequestWebDTO.toInfractionReport(), createInfractionReportRequestWebDTO.getRequestIdentifier());
        return InfractionReportCreatedDTO.from(infractionReport);
    }

}
