package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.jdpi.ports.claim.*;
import com.picpay.banking.jdpi.ports.infraction.InfractionReportPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.RemovePixKeyPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.*;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "pix.partner", havingValue = "bacen")
public class BacenPortBeansConfig {

    @Bean
    public FeignClientInterceptor feignClientInterceptor(TokenManagerClient tokenManagerClient,
                                                         TimeLimiterExecutor timeLimiterExecutor) {
        return new FeignClientInterceptor(tokenManagerClient, timeLimiterExecutor);
    }

    @Bean
    public CancelClaimPort claimCancelPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new ClaimCancelPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public ConfirmationClaimPort claimConfirmationPort(ClaimJDClient claimJDClient,
                                                       TimeLimiterExecutor timeLimiterExecutor) {
        return new ClaimConfirmationPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public CompleteClaimPort completeClaimPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new CompleteClaimPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public FindClaimPort findClaimPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new FindClaimPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public ListClaimPort listClaimPort(ClaimJDClient claimJDClient,
                                       ListClaimConverter listClaimConverter,
                                       TimeLimiterExecutor timeLimiterExecutor) {
        return new ListClaimPortImpl(claimJDClient, listClaimConverter, timeLimiterExecutor);
    }

    @Bean
    public ListPendingClaimPort listPendingClaimPort(ClaimJDClient claimJDClient,
                                                     ListClaimConverter listClaimConverter,
                                                     TimeLimiterExecutor timeLimiterExecutor) {
        return new ListPendingClaimPortImpl(claimJDClient, listClaimConverter, timeLimiterExecutor);
    }

    @Bean
    public RemovePixKeyPort removePixKeyPort(PixKeyJDClient pixKeyJDClient,
                                             TimeLimiterExecutor timeLimiterExecutor) {
        return new RemovePixKeyPortImpl(pixKeyJDClient, timeLimiterExecutor);
    }

    @Bean
    public InfractionReportPort infractionReportPort(InfractionReportJDClient infractionReportJDClient,
                                                     TimeLimiterExecutor timeLimiterExecutor) {
        return new InfractionReportPortImpl(infractionReportJDClient, timeLimiterExecutor);
    }

}
