package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.bacen.CancelInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCacheSavePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCancelPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportListPort;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Value("${picpay.ispb}")
    private String ispbPicPay;

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(CreateInfractionReportPort infractionReportPort,
                                                                       InfractionReportSavePort infractionReportSavePort,
                                                                       InfractionReportFindPort infractionReportFindPort,
                                                                       InfractionReportCacheSavePort infractionReportCacheSavePort) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportSavePort, infractionReportFindPort,
                                                 infractionReportCacheSavePort, ispbPicPay);
    }

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportFindPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public CancelInfractionReportUseCase cancelInfractionReportUseCase(final CancelInfractionReportPort cancelInfractionReportPort,
                                                                       final InfractionReportCancelPort infractionReportCancelPort,
                                                                       final InfractionReportFindPort infractionReportFindPort) {
        return new CancelInfractionReportUseCase(cancelInfractionReportPort, infractionReportCancelPort, infractionReportFindPort);
    }


    @Bean
    public FilterInfractionReportUseCase filterInfractionReportUseCase(InfractionReportListPort infractionReportListPort) {
        return new FilterInfractionReportUseCase(infractionReportListPort);
    }

    @Bean
    public AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase(InfractionReportAnalyzePort analyzeInfractionReportPort,
                                                                         InfractionReportSavePort infractionReportSavePort,
                                                                         InfractionReportFindPort infractionReportFindPort) {
        return new AnalyzeInfractionReportUseCase(analyzeInfractionReportPort, infractionReportFindPort, infractionReportSavePort, ispbPicPay);
    }

}
