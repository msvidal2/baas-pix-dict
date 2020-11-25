package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.ListPendingInfractionReportUseCase;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(InfractionReportPort infractionReportPort,
                                                                       InfractionReportSavePort infractionReportSavePort,
                                                                       IdempotencyValidator<InfractionReport> validator) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportSavePort, validator);
    }

    @Bean
    public ListPendingInfractionReportUseCase listPendingInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new ListPendingInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportFindPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public CancelInfractionReportUseCase cancelInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new CancelInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new AnalyzeInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public FilterInfractionReportUseCase filterInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new FilterInfractionReportUseCase(infractionReportPort);
    }

}
