package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmationClaimPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.*;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.usecase.claim.ClaimCancelUseCase;
import com.picpay.banking.pix.core.usecase.claim.ClaimConfirmationUseCase;
import com.picpay.banking.pix.core.usecase.claim.CompleteClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.CreateClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.FindClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.ListClaimUseCase;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaimUseCaseBeansConfig {

    @Bean
    public CreateClaimUseCase createClaimUseCase(CreateClaimBacenPort createClaimPort,
                                                 CreateClaimPort saveClaimPort,
                                                 FindOpenClaimByKeyPort findClaimByKeyPort,
                                                 FindPixKeyPort findPixKeyPort,
                                                 @Qualifier("createClaimItemValidator") DictItemValidator dictItemValidator) {
        return new CreateClaimUseCase(createClaimPort, saveClaimPort, findClaimByKeyPort, findPixKeyPort, dictItemValidator);
    }

    @Bean
    public ClaimConfirmationUseCase claimConfirmationUseCase(ConfirmationClaimPort claimConfirmationPort,
                                                             @Qualifier("confirmationClaimItemValidator") DictItemValidator dictItemValidator) {
        return new ClaimConfirmationUseCase(claimConfirmationPort,dictItemValidator);
    }

    @Bean
    public ListClaimUseCase listClaimUseCase(ListPendingClaimPort listPendingClaimPort,
                                             ListClaimPort listClaimPort,
                                             @Qualifier("listClaimItemValidator") DictItemValidator dictItemValidator) {
        return new ListClaimUseCase(listPendingClaimPort,listClaimPort,dictItemValidator);
    }

    @Bean
    public ClaimCancelUseCase claimCancelUseCase(CancelClaimPort claimCancelPort,
                                                 @Qualifier("claimCancelItemValidator") DictItemValidator dictItemValidator) {
        return new ClaimCancelUseCase(claimCancelPort, dictItemValidator);
    }

    @Bean
    public CompleteClaimUseCase completeClaimUseCase(CompleteClaimBacenPort completeClaimBacenPort,
                                                     CompleteClaimPort completeClaimPort,
                                                     FindClaimPort findClaimPort,
                                                     @Qualifier("completeClaimItemValidator") DictItemValidator dictItemValidator) {
        return new CompleteClaimUseCase(completeClaimBacenPort, completeClaimPort, findClaimPort, dictItemValidator);
    }

    @Bean
    public FindClaimUseCase findClaimUseCase(FindClaimPort findClaimPort) {
        return new FindClaimUseCase(findClaimPort);
    }

}
