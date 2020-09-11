package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.InfractionReportPort;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.FindInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.ListPendingInfractionReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new CreateInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public ListPendingInfractionReportUseCase listPendingInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new ListPendingInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

}
