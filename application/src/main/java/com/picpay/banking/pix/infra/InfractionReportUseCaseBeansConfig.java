package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.InfractionReportPort;
import com.picpay.banking.pix.core.usecase.InfractionReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public InfractionReportUseCase createAddressKeyUseCase(InfractionReportPort infractionReportPort) {
        return new InfractionReportUseCase(infractionReportPort);
    }

}
