package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.*;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.interceptors.FeignClientInterceptor;
import com.picpay.banking.pix.original.ports.*;
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
    public ClaimCancelPort claimCancelPort() {
        return new ClaimCancelPortImpl();
    }

    @Bean
    public ClaimConfirmationPort claimConfirmationPort() {
        return new ClaimConfirmationPortImpl();
    }

    @Bean
    public CompleteClaimPort completeClaimPort() {
        return new CompleteClaimPortImpl();
    }

    @Bean
    public CreatePixKeyPort createPixKeyPort(final MaintenancePixKeyClient maintenancePixKeyClient) {
        return new CreatePixKeyPortImpl(maintenancePixKeyClient);
    }

    @Bean
    public CreateClaimPort createClaimPort() {
        return new CreateClaimPortImpl();
    }

    @Bean
    public FindClaimPort findClaimPort() {
        return new FindClaimPortImpl();
    }

    @Bean
    public FindPixKeyPort findPixKeyPort() {
        return new FindPixKeyPortImpl();
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
    public ListPixKeyPort listPixKeyPort() {
        return new ListPixKeyPortImpl();
    }

    @Bean
    public RemovePixKeyPort removePixKeyPort() {
        return new RemovePixKeyPortImpl();
    }

    @Bean
    public UpdateAccountPixKeyPort updateAccountPixKeyPort(final MaintenancePixKeyClient maintenancePixKeyClient) {
        return new UpdateAccountPixKeyPortImpl(maintenancePixKeyClient);
    }

}
