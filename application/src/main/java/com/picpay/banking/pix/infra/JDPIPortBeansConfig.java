package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.converter.CreatePixKeyConverter;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.interceptors.FeignClientInterceptor;
import com.picpay.banking.jdpi.ports.ClaimCancelPortImpl;
import com.picpay.banking.jdpi.ports.ClaimConfirmationPortImpl;
import com.picpay.banking.jdpi.ports.CompleteClaimPortImpl;
import com.picpay.banking.jdpi.ports.CreateClaimPortImpl;
import com.picpay.banking.jdpi.ports.CreatePixKeyPortImpl;
import com.picpay.banking.jdpi.ports.FindClaimPortImpl;
import com.picpay.banking.jdpi.ports.FindPixKeyPortImpl;
import com.picpay.banking.jdpi.ports.InfractionReportPortImpl;
import com.picpay.banking.jdpi.ports.ListClaimPortImpl;
import com.picpay.banking.jdpi.ports.ListPendingClaimPortImpl;
import com.picpay.banking.jdpi.ports.ListPixKeyPortImpl;
import com.picpay.banking.jdpi.ports.RemovePixKeyPortImpl;
import com.picpay.banking.jdpi.ports.UpdateAccountPixKeyPortImpl;
import com.picpay.banking.pix.core.ports.ClaimCancelPort;
import com.picpay.banking.pix.core.ports.ClaimConfirmationPort;
import com.picpay.banking.pix.core.ports.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.CreateClaimPort;
import com.picpay.banking.pix.core.ports.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.FindClaimPort;
import com.picpay.banking.pix.core.ports.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import com.picpay.banking.pix.core.ports.ListClaimPort;
import com.picpay.banking.pix.core.ports.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.UpdateAccountPixKeyPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnProperty(value = "pix.partner", havingValue = "jdpi")
public class JDPIPortBeansConfig {

    @Bean
    public FeignClientInterceptor feignClientInterceptor(TokenManagerClient tokenManagerClient){
        return new FeignClientInterceptor(tokenManagerClient);
    }

    @Bean
    public ClaimCancelPort claimCancelPort(ClaimJDClient claimJDClient) {
        return new ClaimCancelPortImpl(claimJDClient);
    }

    @Bean
    public ClaimConfirmationPort claimConfirmationPort(ClaimJDClient claimJDClient) {
        return new ClaimConfirmationPortImpl(claimJDClient);
    }

    @Bean
    public CompleteClaimPort completeClaimPort(ClaimJDClient claimJDClient) {
        return new CompleteClaimPortImpl(claimJDClient);
    }

    @Bean
    public CreatePixKeyPort createPixKeyPort(PixKeyJDClient pixKeyJDClient, CreatePixKeyConverter converter) {
        return new CreatePixKeyPortImpl(pixKeyJDClient,converter);
    }

    @Bean
    public CreateClaimPort createClaimPort(ClaimJDClient claimJDClient, CreateClaimConverter createClaimConverter) {
        return new CreateClaimPortImpl(claimJDClient,createClaimConverter);
    }

    @Bean
    public FindPixKeyPort findPixKeyPort(PixKeyJDClient pixKeyJDClient,
                                                       FindPixKeyConverter findPixKeyConverter) {
        return new FindPixKeyPortImpl(pixKeyJDClient, findPixKeyConverter);
    }

    @Bean
    public FindClaimPort findClaimPort(ClaimJDClient claimJDClient, CreateClaimConverter createClaimConverter) {
        return new FindClaimPortImpl(claimJDClient,createClaimConverter);
    }

    @Bean
    public ListPixKeyPort listPixKeyPort(PixKeyJDClient pixKeyJDClient, ListPixKeyConverter listPixKeyConverter) {
        return new ListPixKeyPortImpl(pixKeyJDClient, listPixKeyConverter);
    }

    @Bean
    public ListClaimPort listClaimPort(ClaimJDClient claimJDClient, ListClaimConverter listClaimConverter) {
        return new ListClaimPortImpl(claimJDClient,listClaimConverter);
    }

    @Bean
    public ListPendingClaimPort listPendingClaimPort(ClaimJDClient claimJDClient, ListClaimConverter listClaimConverter) {
        return new ListPendingClaimPortImpl(claimJDClient,listClaimConverter);
    }

    @Bean
    public RemovePixKeyPort removePixKeyPort(PixKeyJDClient pixKeyJDClient, CreateClaimConverter createClaimConverter) {
        return new RemovePixKeyPortImpl(pixKeyJDClient);
    }

    @Bean
    public UpdateAccountPixKeyPort updateAccountPixKeyPort(PixKeyJDClient pixKeyJDClient) {
        return new UpdateAccountPixKeyPortImpl(pixKeyJDClient);
    }

    @Bean
    public InfractionReportPort infractionReportPort(InfractionReportJDClient infractionReportJDClient) {
        return new InfractionReportPortImpl(infractionReportJDClient);
    }

}
