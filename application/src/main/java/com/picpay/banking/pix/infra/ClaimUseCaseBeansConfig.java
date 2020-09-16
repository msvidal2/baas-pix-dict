package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.ClaimCancelPort;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
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
    public CreateClaimUseCase createClaimUseCase(CreateClaimPort createClaimPort,
                                                 @Qualifier("createClaimItemValidator") DictItemValidator dictItemValidator) {
        return new CreateClaimUseCase(createClaimPort, dictItemValidator);
    }

    @Bean
    public ClaimConfirmationUseCase claimConfirmationUseCase(ClaimConfirmationPort claimConfirmationPort,
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
    public ClaimCancelUseCase claimCancelUseCase(ClaimCancelPort claimCancelPort,
                                                 @Qualifier("claimCancelItemValidator") DictItemValidator dictItemValidator) {
        return new ClaimCancelUseCase(claimCancelPort, dictItemValidator);
    }

    @Bean
    public CompleteClaimUseCase completeClaimUseCase(CompleteClaimPort completeClaimPort,
                                                     @Qualifier("completeClaimItemValidator") DictItemValidator dictItemValidator) {
        return new CompleteClaimUseCase(completeClaimPort, dictItemValidator);
    }

    @Bean
    public FindClaimUseCase findClaimUseCase(FindClaimPort findClaimPort,
                                             @Qualifier("findClaimItemValidator") DictItemValidator dictItemValidator) {
        return new FindClaimUseCase(findClaimPort, dictItemValidator);
    }

}
