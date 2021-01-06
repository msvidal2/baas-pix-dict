package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.ListPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateAccountPixKeyUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PixKeyUseCaseBeansConfig {

    @Bean
    public CreatePixKeyUseCase createPixKeyUseCase(CreatePixKeyBacenPort createPixKeyBacenPort,
                                                   SavePixKeyPort savePixKeyPort,
                                                   FindPixKeyPort findPixKeyPort,
                                                   FindOpenClaimByKeyPort findOpenClaimByKeyPort) {
        return new CreatePixKeyUseCase(createPixKeyBacenPort, savePixKeyPort, findPixKeyPort, findOpenClaimByKeyPort);
    }

    @Bean
    public RemovePixKeyUseCase removePixKeyUseCase(RemovePixKeyPort removePixKeyPort,
                                                   RemovePixKeyBacenPort removePixKeyBacenPort) {
        return new RemovePixKeyUseCase(removePixKeyPort, removePixKeyBacenPort);
    }

    @Bean
    public FindPixKeyUseCase findPixKeyUseCase(FindPixKeyPort findPixKeyPort,
                                               FindPixKeyBacenPort findPixKeyBacenPort) {
        return new FindPixKeyUseCase(findPixKeyPort, findPixKeyBacenPort);
    }

    @Bean
    public ListPixKeyUseCase listPixKeyUseCase(ListPixKeyPort listPixKeyPort) {
        return new ListPixKeyUseCase(listPixKeyPort);
    }

    @Bean
    public UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase(SavePixKeyPort savePixKeyPort,
                                                                 UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort) {
        return new UpdateAccountPixKeyUseCase(savePixKeyPort, updateAccountPixKeyBacenPort);
    }
}
