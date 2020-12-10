package com.picpay.banking.config;

import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionAcknowledgePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.InfractionAcknowledgeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 08/12/20
 */
@Configuration
public class InfractionConfig {

    @Bean
    public InfractionAcknowledgeUseCase infractionAcknowledgeUseCase(InfractionReportSavePort infractionReportSavePort,
                                                                     InfractionNotificationPort infractionNotificationPort,
                                                                     InfractionAcknowledgePort infractionAcknowledgePort) {
        return new InfractionAcknowledgeUseCase(infractionReportSavePort, infractionNotificationPort, infractionAcknowledgePort);
    }

}
