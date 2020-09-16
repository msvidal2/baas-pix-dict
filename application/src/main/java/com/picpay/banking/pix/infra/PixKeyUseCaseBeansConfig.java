package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.*;
import com.picpay.banking.pix.core.usecase.pixkey.*;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PixKeyUseCaseBeansConfig {

    @Bean
    public CreatePixKeyUseCase createPixKeyUseCase(CreatePixKeyPort createPixKeyPort,
                                                   @Qualifier("createPixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new CreatePixKeyUseCase(createPixKeyPort, dictItemValidator);
    }

    @Bean
    public RemovePixKeyUseCase removePixKeyUseCase(RemovePixKeyPort removePixKeyPort,
                                                   @Qualifier("removePixKeyItemValidator") DictItemValidator dictItemValidator) {
        return new RemovePixKeyUseCase(removePixKeyPort, dictItemValidator);
    }

    @Bean
    public FindPixKeyUseCase findPixKeyUseCase(FindPixKeyPort findPixKeyPort) {
        return new FindPixKeyUseCase(findPixKeyPort);
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
