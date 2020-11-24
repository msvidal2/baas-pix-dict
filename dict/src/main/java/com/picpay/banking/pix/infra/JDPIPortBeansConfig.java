package com.picpay.banking.pix.infra;

import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.ports.bacen.InfractionReportPortImpl;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.jdpi.ports.claim.*;
import com.picpay.banking.jdpi.ports.infraction.InfractionReportPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.*;
import com.picpay.banking.pix.core.ports.claim.bacen.*;
import com.picpay.banking.jdpi.ports.claim.ClaimCancelPortImpl;
import com.picpay.banking.jdpi.ports.claim.ClaimConfirmationPortImpl;
import com.picpay.banking.jdpi.ports.claim.CompleteClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.CreateClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.FindClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.ListClaimPortImpl;
import com.picpay.banking.jdpi.ports.claim.ListPendingClaimPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.FindPixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.ListPixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.RemovePixKeyPortImpl;
import com.picpay.banking.jdpi.ports.pixkey.UpdateAccountPixKeyPortImpl;
import com.picpay.banking.pix.core.ports.claim.ClaimCancelPort;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.ListPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.*;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
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

//    @Bean
//    public CreatePixKeyPort createPixKeyPort(PixKeyJDClient pixKeyJDClient,
//                                             CreatePixKeyConverter converter,
//                                             TimeLimiterExecutor timeLimiterExecutor) {
//        return new CreatePixKeyPortImpl(pixKeyJDClient, converter, timeLimiterExecutor);
//    }

    @Bean
    public CreateClaimBacenPort createClaimPort(ClaimJDClient claimJDClient,
                                                CreateClaimConverter createClaimConverter,
                                                TimeLimiterExecutor timeLimiterExecutor) {
        return new CreateClaimPortImpl(claimJDClient, createClaimConverter, timeLimiterExecutor);
    }

    @Bean
    public FindPixKeyPort findPixKeyPort(final PixKeyJDClient pixKeyJDClient,
                                         final FindPixKeyConverter findPixKeyConverter,
                                         final TimeLimiterExecutor timeLimiterExecutor) {
        return new FindPixKeyPortImpl(pixKeyJDClient, findPixKeyConverter, timeLimiterExecutor);
    }

    @Bean
    public FindClaimPort findClaimPort(ClaimJDClient claimJDClient, TimeLimiterExecutor timeLimiterExecutor) {
        return new FindClaimPortImpl(claimJDClient, timeLimiterExecutor);
    }

    @Bean
    public ListPixKeyPort listPixKeyPort(PixKeyJDClient pixKeyJDClient,
                                         ListPixKeyConverter listPixKeyConverter,
                                         TimeLimiterExecutor timeLimiterExecutor) {
        return new ListPixKeyPortImpl(pixKeyJDClient, listPixKeyConverter, timeLimiterExecutor);
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
    public InfractionReportPort infractionReportPort(final CreateInfractionBacenClient bacenClient,
                                                     final TimeLimiterExecutor timeLimiterExecutor) {
        return new InfractionReportPortImpl(bacenClient, timeLimiterExecutor);
    }

}
