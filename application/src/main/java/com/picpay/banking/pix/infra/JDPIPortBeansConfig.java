package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.*;
import com.picpay.banking.jdpi.ports.*;
import com.picpay.banking.pix.core.ports.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JDPIPortBeansConfig {

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

}
