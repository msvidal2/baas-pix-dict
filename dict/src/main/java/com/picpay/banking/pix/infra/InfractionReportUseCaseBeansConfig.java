package com.picpay.banking.pix.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.idempotency.IdempotencyValidatorImpl;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.AnalyzeInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportListPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.ports.infraction.*;
import com.picpay.banking.pix.core.usecase.infraction.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FilterInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.infraction.FindInfractionReportUseCase;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class InfractionReportUseCaseBeansConfig {

    @Bean
    public CreateInfractionReportUseCase createInfractionReportUseCase(CreateInfractionReportPort infractionReportPort,
                                                                       InfractionReportSavePort infractionReportSavePort,
                                                                       IdempotencyValidator<InfractionReport> validator,
                                                                       InfractionReportFindPort infractionReportFindPort) {
        return new CreateInfractionReportUseCase(infractionReportPort, infractionReportSavePort, infractionReportFindPort, validator);
    }

    @Bean
    public FindInfractionReportUseCase findInfractionReportUseCase(InfractionReportFindPort infractionReportPort) {
        return new FindInfractionReportUseCase(infractionReportPort);
    }

    @Bean
    public IdempotencyValidator<InfractionReport> idempotencyValidator(final RedisTemplate<String, Object> redisTemplate, final ObjectMapper objectMapper) {
        return new IdempotencyValidatorImpl<>(redisTemplate, objectMapper, InfractionReport.class);
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

    @Bean
    public CancelInfractionReportUseCase cancelInfractionReportUseCase(final CancelInfractionReportPort cancelInfractionReportPort,
        final InfractionReportCancelPort infractionReportCancelPort) {
        return new CancelInfractionReportUseCase(cancelInfractionReportPort, infractionReportCancelPort);
    }

}
