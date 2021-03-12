package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import com.picpay.banking.pix.core.ports.claim.bacen.*;
import com.picpay.banking.pix.core.ports.claim.picpay.*;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.usecase.claim.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClaimUseCaseBeansConfig {

    private final CreateClaimBacenPort createClaimPort;
    private final CreateClaimPort saveClaimPort;
    private final FindOpenClaimByKeyPort findClaimByKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final ConfirmClaimPort claimConfirmationPort;
    private final FindClaimPort findClaimPort;
    private final RemovePixKeyPort removePixKeyPort;
    private PixKeyEventPort pixKeyEventPort;
    private final ListClaimPort listClaimPort;
    private final CancelClaimBacenPort claimCancelPort;
    private final FindByIdPort findByIdPort;
    private final CancelClaimPort cancelClaimPort;
    private final SavePixKeyPort savePixKeyPort;
    private final CompleteClaimBacenPort completeClaimBacenPort;
    private final CompleteClaimPort completeClaimPort;
    private final SavePixKeyPort createPixKeyPort;

    @Bean
    public CreateClaimUseCase createClaimUseCase() {
        return new CreateClaimUseCase(createClaimPort, saveClaimPort, findClaimByKeyPort, findPixKeyPort);
    }

    @Bean
    public ConfirmClaimUseCase claimConfirmationUseCase() {
        return new ConfirmClaimUseCase(claimConfirmationPort, findClaimPort, saveClaimPort, removePixKeyPort, pixKeyEventPort, findPixKeyPort);
    }

    @Bean
    public ListClaimUseCase listClaimUseCase() {
        return new ListClaimUseCase(listClaimPort);
    }

    @Bean
    public CancelClaimUseCase claimCancelUseCase() {
        return new CancelClaimUseCase(claimCancelPort, findByIdPort, cancelClaimPort, findPixKeyPort, savePixKeyPort);
    }

    @Bean
    public CompleteClaimUseCase completeClaimUseCase() {
        return new CompleteClaimUseCase(completeClaimBacenPort, completeClaimPort, findClaimPort, createPixKeyPort, pixKeyEventPort);
    }

    @Bean
    public FindClaimUseCase findClaimUseCase(FindClaimPort findClaimPort) {
        return new FindClaimUseCase(findClaimPort);
    }

    @Bean
    public ClaimEventRegistryUseCase claimEventRegistryUseCase(ClaimEventRegistryPort claimEventRegistryPort) {
        return new ClaimEventRegistryUseCase(claimEventRegistryPort);
    }

}
