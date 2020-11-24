package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportListPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(
        InfractionReportPort infractionReportPort, InfractionReportSavePort infractionReportSavePort) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportSavePort);
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
    public FilterInfractionReportUseCase filterInfractionReportUseCase(InfractionReportListPort infractionReportListPort) {
        return new FilterInfractionReportUseCase(infractionReportListPort);
    }

}
