package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportListPort;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.InfractionEventRegistryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportFindPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public FilterInfractionReportUseCase filterInfractionReportUseCase(InfractionReportListPort infractionReportListPort) {
        return new FilterInfractionReportUseCase(infractionReportListPort);
    }

    @Bean
    public InfractionEventRegistryUseCase infractionEventRegistryUseCase(InfractionEventRegistryPort infractionEventRegistryPort) {
        return new InfractionEventRegistryUseCase(infractionEventRegistryPort);
    }
}
