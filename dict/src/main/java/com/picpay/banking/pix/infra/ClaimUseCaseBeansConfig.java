package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.ports.claim.bacen.*;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.usecase.claim.ClaimCancelUseCase;
import com.picpay.banking.pix.core.usecase.claim.ConfirmClaimUseCase;
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
    public ConfirmClaimUseCase claimConfirmationUseCase(ConfirmClaimPort claimConfirmationPort) {
        return new ConfirmClaimUseCase(claimConfirmationPort);
    }

    @Bean
    public ListClaimUseCase listClaimUseCase(ListPendingClaimPort listPendingClaimPort,
                                             ListClaimPort listClaimPort,
                                             @Qualifier("listClaimItemValidator") DictItemValidator dictItemValidator) {
        return new ListClaimUseCase(listPendingClaimPort,listClaimPort,dictItemValidator);
    }

    @Bean
    public ClaimCancelUseCase claimCancelUseCase(CancelClaimPort claimCancelPort) {
        return new ClaimCancelUseCase(claimCancelPort);
    }

    @Bean
    public CompleteClaimUseCase completeClaimUseCase(CompleteClaimPort completeClaimPort,
                                                     @Qualifier("completeClaimItemValidator") DictItemValidator dictItemValidator) {
        return new CompleteClaimUseCase(completeClaimPort, dictItemValidator);
    }

    @Bean
    public FindClaimUseCase findClaimUseCase(FindClaimPort findClaimPort) {
        return new FindClaimUseCase(findClaimPort);
    }

}
