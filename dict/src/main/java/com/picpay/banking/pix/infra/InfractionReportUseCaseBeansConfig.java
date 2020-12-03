package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.infraction.*;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(CreateInfractionReportPort infractionReportPort,
                                                                       InfractionReportSavePort infractionReportSavePort,
                                                                       InfractionReportFindPort infractionReportFindPort) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportSavePort, infractionReportFindPort);
    }

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportFindPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public CancelInfractionReportUseCase cancelInfractionReportUseCase(final CancelInfractionReportPort cancelInfractionReportPort,
                                                                       final InfractionReportCancelPort infractionReportCancelPort) {
        return new CancelInfractionReportUseCase(cancelInfractionReportPort, infractionReportCancelPort);
    }


    @Bean
    public FilterInfractionReportUseCase filterInfractionReportUseCase(InfractionReportListPort infractionReportListPort) {
        return new FilterInfractionReportUseCase(infractionReportListPort);
    }

    @Bean
    public AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase(AnalyzeInfractionReportPort analyzeInfractionReportPort,
        InfractionReportAnalyzePort infractionReportAnalyzePort, InfractionReportFindPort infractionReportFindPort) {
        return new AnalyzeInfractionReportUseCase(analyzeInfractionReportPort, infractionReportAnalyzePort, infractionReportFindPort);
    }

}
