package com.picpay.banking.pix.infra;

import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.ports.bacen.CreateInfractionReportPortImpl;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.jdpi.ports.claim.ClaimCancelPortImpl;
import com.picpay.banking.jdpi.ports.claim.ClaimConfirmationPortImpl;
import com.picpay.banking.jdpi.ports.claim.CompleteClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.CreateClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.FindClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.ListClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.ListPendingClaimPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.ListPixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.RemovePixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.UpdateAccountPixKeyPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmationClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pixkey.ports.picpay.FindPixKeyPortImpl;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import com.picpay.banking.jdpi.ports.claim.*;
import com.picpay.banking.jdpi.ports.infraction.InfractionReportPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.RemovePixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.UpdateAccountPixKeyPortImpl;
import com.picpay.banking.pix.core.ports.claim.bacen.*;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "pix.partner", havingValue = "jdpi")
public class JDPIPortBeansConfig {

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
    public UpdateAccountPixKeyPort updateAccountPixKeyPort(PixKeyJDClient pixKeyJDClient,
                                                           TimeLimiterExecutor timeLimiterExecutor) {
        return new UpdateAccountPixKeyPortImpl(pixKeyJDClient, timeLimiterExecutor);
    }

    @Bean
    public CreateInfractionReportPort infractionReportPort(final CreateInfractionBacenClient bacenClient,
                                                           final TimeLimiterExecutor timeLimiterExecutor) {
        return new CreateInfractionReportPortImpl(bacenClient, timeLimiterExecutor);
    }

}
