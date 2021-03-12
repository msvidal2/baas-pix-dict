package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.usecase.pixkey.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.ListPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PixKeyUseCaseBeansConfig {

    private final FindPixKeyPort findPixKeyPort;
    private final FindPixKeyBacenPort findPixKeyBacenPort;
    private final ListPixKeyPort listPixKeyPort;
    private final PixKeyEventRegistryPort eventRegistryPort;

    @Bean
    public FindPixKeyUseCase findPixKeyUseCase() {
        return new FindPixKeyUseCase(findPixKeyPort, findPixKeyBacenPort);
    }

    @Bean
    public ListPixKeyUseCase listPixKeyUseCase() {
        return new ListPixKeyUseCase(listPixKeyPort);
    }

    @Bean
    public PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase() {
        return new PixKeyEventRegistryUseCase(eventRegistryPort);
    }

}
