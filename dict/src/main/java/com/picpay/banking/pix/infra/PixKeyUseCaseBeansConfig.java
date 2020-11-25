package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PixKeyUseCaseBeansConfig {

    @Bean
    public CreatePixKeyUseCase createPixKeyUseCase(CreatePixKeyBacenPort createPixKeyBacenPort,
                                                   CreatePixKeyPort createPixKeyPort,
                                                   FindPixKeyPort findPixKeyPort) {
        return new CreatePixKeyUseCase(createPixKeyBacenPort, createPixKeyPort, findPixKeyPort);
    }

    @Bean
    public RemovePixKeyUseCase removePixKeyUseCase(RemovePixKeyPort removePixKeyPort,
                                                   @Qualifier("removePixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new RemovePixKeyUseCase(removePixKeyPort, dictItemValidator);
    }

    @Bean
    public FindPixKeyUseCase findPixKeyUseCase(FindPixKeyPort findPixKeyPort,
                                               FindPixKeyBacenPort findPixKeyBacenPort) {
        return new FindPixKeyUseCase(findPixKeyPort, findPixKeyBacenPort);
    }

    @Bean
    public ListPixKeyUseCase listPixKeyUseCase(ListPixKeyPort listPixKeyPort,
                                               @Qualifier("listPixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new ListPixKeyUseCase(listPixKeyPort, dictItemValidator);
    }

    @Bean
    public UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase(UpdateAccountPixKeyPort updateAccountPixKeyPort,
                                                                 UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort) {
        return new UpdateAccountPixKeyUseCase(updateAccountPixKeyPort, updateAccountPixKeyBacenPort);
    }
}
