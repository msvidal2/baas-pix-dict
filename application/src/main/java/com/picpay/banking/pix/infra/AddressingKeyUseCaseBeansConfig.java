package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.CreateAddressingKeyPort;
import com.picpay.banking.pix.core.ports.FindAddressingKeyPort;
import com.picpay.banking.pix.core.ports.ListAddressingKeyPort;
import com.picpay.banking.pix.core.ports.RemoveAddressingKeyPort;
import com.picpay.banking.pix.core.ports.UpdateAccountAddressingKeyPort;
import com.picpay.banking.pix.core.usecase.CreateAddressingKeyUseCase;
import com.picpay.banking.pix.core.usecase.FindAddressKeyUseCase;
import com.picpay.banking.pix.core.usecase.ListAddressKeyUseCase;
import com.picpay.banking.pix.core.usecase.RemoveAddressingKeyUseCase;
import com.picpay.banking.pix.core.usecase.UpdateAccountAddressingKeyUseCase;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressingKeyUseCaseBeansConfig {

    @Bean
    public CreateAddressingKeyUseCase createAddressKeyUseCase(CreateAddressingKeyPort createAddressingKeyPort
            , @Qualifier("createAddressingKeyItemValidator") DictItemValidator dictItemValidator) {
        return new CreateAddressingKeyUseCase(createAddressingKeyPort, dictItemValidator);
    }

    @Bean
    public RemoveAddressingKeyUseCase removeAddressingKeyUseCase(RemoveAddressingKeyPort removeAddressingKeyPort
            , @Qualifier("removeAddressingKeyItemValidator") DictItemValidator dictItemValidator) {
        return new RemoveAddressingKeyUseCase(removeAddressingKeyPort, dictItemValidator);
    }

    @Bean
    public FindAddressKeyUseCase findAddressKeyUseCase(FindAddressingKeyPort findAddressingKeyPort
            , @Qualifier("findAddressingKeyItemValidator") DictItemValidator dictItemValidator) {
        return new FindAddressKeyUseCase(findAddressingKeyPort, dictItemValidator);
    }

    @Bean
    public ListAddressKeyUseCase listAddressKeyUseCase(ListAddressingKeyPort listAddressingKeyPort
            , @Qualifier("listAddressingKeyItemValidator") DictItemValidator dictItemValidator) {
        return new ListAddressKeyUseCase(listAddressingKeyPort, dictItemValidator);
    }

    @Bean
    public UpdateAccountAddressingKeyUseCase updateAccountAddressingKeyUseCase(
            UpdateAccountAddressingKeyPort updateAccountAddressingKeyPort
            , @Qualifier("updateAddressingKeyItemValidator") DictItemValidator dictItemValidator) {
        return new UpdateAccountAddressingKeyUseCase(updateAccountAddressingKeyPort, dictItemValidator);
    }
}
