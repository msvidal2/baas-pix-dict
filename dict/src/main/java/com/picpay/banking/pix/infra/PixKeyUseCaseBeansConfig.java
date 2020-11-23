package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
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
    public FindPixKeyUseCase findPixKeyUseCase(@Qualifier("FindPixKeyPort") FindPixKeyPort findPixKeyPort,
                                               @Qualifier("FindPixKeyBacenPort") FindPixKeyPort findPixKeyBacenPort) {
        return new FindPixKeyUseCase(findPixKeyPort, findPixKeyBacenPort);
    }

    @Bean
    public ListPixKeyUseCase listPixKeyUseCase(ListPixKeyPort listPixKeyPort,
                                               @Qualifier("listPixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new ListPixKeyUseCase(listPixKeyPort, dictItemValidator);
    }

    @Bean
    public UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase(UpdateAccountPixKeyPort updateAccountPixKeyPort,
                                                                 @Qualifier("updatePixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new UpdateAccountPixKeyUseCase(updateAccountPixKeyPort, dictItemValidator);
    }
}
