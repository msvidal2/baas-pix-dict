package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ReconciliationSyncEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.ListPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.RemovePixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateAccountPixKeyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PixKeyUseCaseBeansConfig {

    private final CreatePixKeyBacenPort createPixKeyBacenPort;
    private final CreatePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final FindOpenClaimByKeyPort findOpenClaimByKeyPort;
    private final ReconciliationSyncEventPort reconciliationSyncEventPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final RemovePixKeyBacenPort removePixKeyBacenPort;
    private final FindPixKeyBacenPort findPixKeyBacenPort;
    private final ListPixKeyPort listPixKeyPort;
    private final UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private final UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;

    @Bean
    public CreatePixKeyUseCase createPixKeyUseCase() {
        return new CreatePixKeyUseCase(createPixKeyBacenPort, createPixKeyPort, findPixKeyPort, findOpenClaimByKeyPort,
            reconciliationSyncEventPort);
    }

    @Bean
    public RemovePixKeyUseCase removePixKeyUseCase() {
        return new RemovePixKeyUseCase(removePixKeyPort, removePixKeyBacenPort, reconciliationSyncEventPort);
    }

    @Bean
    public FindPixKeyUseCase findPixKeyUseCase() {
        return new FindPixKeyUseCase(findPixKeyPort, findPixKeyBacenPort);
    }

    @Bean
    public ListPixKeyUseCase listPixKeyUseCase() {
        return new ListPixKeyUseCase(listPixKeyPort);
    }

    @Bean
    public UpdateAccountPixKeyUseCase updateAccountPixKeyUseCase() {
        return new UpdateAccountPixKeyUseCase(updateAccountPixKeyPort, updateAccountPixKeyBacenPort, findPixKeyPort,
            reconciliationSyncEventPort);
    }

}
