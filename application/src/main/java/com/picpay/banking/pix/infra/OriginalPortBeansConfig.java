package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.*;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.pixkey.*;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.interceptors.FeignClientInterceptor;
import com.picpay.banking.pix.original.ports.claim.*;
import com.picpay.banking.pix.original.ports.infraction.InfractionReportPortImpl;
import com.picpay.banking.pix.original.ports.pixkey.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "pix.partner", havingValue = "original")
public class OriginalPortBeansConfig {

    @Bean
    public FeignClientInterceptor feignClientInterceptor(){
        return new FeignClientInterceptor();
    }

    @Bean
    public ClaimCancelPort claimCancelPort(final ClaimClient claimClient) {
        return new ClaimCancelPortImpl(claimClient);
    }

    @Bean
    public ClaimConfirmationPort claimConfirmationPort(final ClaimClient claimClient) {
        return new ClaimConfirmationPortImpl(claimClient);
    }

    @Bean
    public CompleteClaimPort completeClaimPort(final ClaimClient claimClient) {
        return new CompleteClaimPortImpl(claimClient);
    }

    @Bean
    public CreatePixKeyPort createPixKeyPort(final AccessKeyClient accessKeyClient) {
        return new CreatePixKeyPortImpl(accessKeyClient);
    }

    @Bean
    public CreateClaimPort createClaimPort(final ClaimClient claimClient) {
        return new CreateClaimPortImpl(claimClient);
    }

    @Bean
    public FindClaimPort findClaimPort(final ClaimClient claimClient) {
        return new FindClaimPortImpl(claimClient);
    }

    @Bean
    public FindPixKeyPort findPixKeyPort(final AccessKeyClient accessKeyClient) {
        return new FindPixKeyPortImpl(accessKeyClient);
    }

    @Bean
    public InfractionReportPort infractionReportPort() {
        return new InfractionReportPortImpl();
    }

    @Bean
    public ListClaimPort listClaimPort() {
        return new ListClaimPortImpl();
    }

    @Bean
    public ListPendingClaimPort listPendingClaimPort() {
        return new ListPendingClaimPortImpl();
    }

    @Bean
    public ListPixKeyPort listPixKeyPort(AccessKeyClient accessKeyClient) {
        return new ListPixKeyPortImpl(accessKeyClient);
    }

    @Bean
    public RemovePixKeyPort removePixKeyPort(AccessKeyClient accessKeyClient) {
        return new RemovePixKeyPortImpl(accessKeyClient);
    }

    @Bean
    public UpdateAccountPixKeyPort updateAccountPixKeyPort(final AccessKeyClient accessKeyClient) {
        return new UpdateAccountPixKeyPortImpl(accessKeyClient);
    }

}
