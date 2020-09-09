package com.picpay.banking.pix.infra;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.CreateAddressingKeyConverter;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.converter.FindAddressingKeyConverter;
import com.picpay.banking.jdpi.converter.ListAddressingKeyConverter;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.ports.ClaimCancelPortImpl;
import com.picpay.banking.jdpi.ports.ClaimConfirmationPortImpl;
import com.picpay.banking.jdpi.ports.CompleteClaimPortImpl;
import com.picpay.banking.jdpi.ports.CreateAddressingKeyPortImpl;
import com.picpay.banking.jdpi.ports.CreateClaimPortImpl;
import com.picpay.banking.jdpi.ports.FindAddressingKeyPortImpl;
import com.picpay.banking.jdpi.ports.FindClaimPortImpl;
import com.picpay.banking.jdpi.ports.ListAddressingKeyPortImpl;
import com.picpay.banking.jdpi.ports.ListClaimPortImpl;
import com.picpay.banking.jdpi.ports.ListPendingClaimPortImpl;
import com.picpay.banking.jdpi.ports.RemoveAddressingKeyPortImpl;
import com.picpay.banking.jdpi.ports.UpdateAccountAddressingKeyPortImpl;
import com.picpay.banking.pix.core.ports.ClaimCancelPort;
import com.picpay.banking.pix.core.ports.ClaimConfirmationPort;
import com.picpay.banking.pix.core.ports.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.CreateAddressingKeyPort;
import com.picpay.banking.pix.core.ports.CreateClaimPort;
import com.picpay.banking.pix.core.ports.FindAddressingKeyPort;
import com.picpay.banking.pix.core.ports.FindClaimPort;
import com.picpay.banking.pix.core.ports.ListAddressingKeyPort;
import com.picpay.banking.pix.core.ports.ListClaimPort;
import com.picpay.banking.pix.core.ports.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.RemoveAddressingKeyPort;
import com.picpay.banking.pix.core.ports.UpdateAccountAddressingKeyPort;
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
    public CreateAddressingKeyPort createAddressingKeyPort(AddressingKeyJDClient addressingKeyJDClient,  CreateAddressingKeyConverter converter) {
        return new CreateAddressingKeyPortImpl(addressingKeyJDClient,converter);
    }

    @Bean
    public CreateClaimPort createClaimPort(ClaimJDClient claimJDClient, CreateClaimConverter createClaimConverter) {
        return new CreateClaimPortImpl(claimJDClient,createClaimConverter);
    }

    @Bean
    public FindAddressingKeyPort findAddressingKeyPort(AddressingKeyJDClient addressingKeyJDClient,
        FindAddressingKeyConverter findAddressingKeyConverter) {
        return new FindAddressingKeyPortImpl(addressingKeyJDClient,findAddressingKeyConverter);
    }

    @Bean
    public FindClaimPort findClaimPort(ClaimJDClient claimJDClient, CreateClaimConverter createClaimConverter) {
        return new FindClaimPortImpl(claimJDClient,createClaimConverter);
    }

    @Bean
    public ListAddressingKeyPort listAddressingKeyPort(AddressingKeyJDClient addressingKeyJDClient, ListAddressingKeyConverter listAddressingKeyConverter) {
        return new ListAddressingKeyPortImpl(addressingKeyJDClient,listAddressingKeyConverter);
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
    public RemoveAddressingKeyPort removeAddressingKeyPort(AddressingKeyJDClient addressingKeyJDClient, CreateClaimConverter createClaimConverter) {
        return new RemoveAddressingKeyPortImpl(addressingKeyJDClient);
    }

    @Bean
    public UpdateAccountAddressingKeyPort updateAccountAddressingKeyPort(AddressingKeyJDClient addressingKeyJDClient) {
        return new UpdateAccountAddressingKeyPortImpl(addressingKeyJDClient);
    }

}
