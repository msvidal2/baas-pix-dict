package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.usecase.claim.CancelClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ClaimEventRegistryUseCase;
import com.picpay.banking.pix.core.usecase.claim.CompleteClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ConfirmClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.CreateClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.FindClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ListClaimUseCase;
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
    private final ListClaimPort listClaimPort;
    private final CancelClaimBacenPort claimCancelPort;
    private final FindByIdPort findByIdPort;
    private final CancelClaimPort cancelClaimPort;
    private final SavePixKeyPort savePixKeyPort;
    private final CompleteClaimBacenPort completeClaimBacenPort;
    private final CompleteClaimPort completeClaimPort;
    private final SavePixKeyPort createPixKeyPort;

    private final ClaimEventRegistryPort claimEventRegistryPort;

    @Bean
    public CreateClaimUseCase createClaimUseCase() {
        return new CreateClaimUseCase(createClaimPort, findClaimByKeyPort, findPixKeyPort);
    }

    @Bean
    public ConfirmClaimUseCase claimConfirmationUseCase() {
        return new ConfirmClaimUseCase(claimConfirmationPort, findClaimPort, saveClaimPort, removePixKeyPort, findPixKeyPort);
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
        return new CompleteClaimUseCase(completeClaimBacenPort, completeClaimPort, findClaimPort, createPixKeyPort);
    }

    @Bean
    public FindClaimUseCase findClaimUseCase(FindClaimPort findClaimPort) {
        return new FindClaimUseCase(findClaimPort);
    }

    @Bean
    public ClaimEventRegistryUseCase claimEventRegistryUseCase() {
        return new ClaimEventRegistryUseCase(claimEventRegistryPort);
    }

}
