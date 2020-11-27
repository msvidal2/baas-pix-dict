package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimAccountNumberItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimAccountOpeningDateItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimBranchNumberItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimCpfCnpjItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimFantasyNameItemValidator;
import com.picpay.banking.pix.core.validators.ClaimIdValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimIspbItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimNameItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import com.picpay.banking.pix.core.validators.claim.IspbClaimItemValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ClaimValidatorConfiguration {

    @Bean
    public DictItemValidator createClaimItemValidator() {
        return new ClaimValidatorComposite(
                List.of(
                        new ClaimAccountNumberItemValidator(),
                        new ClaimAccountOpeningDateItemValidator(),
                        new ClaimBranchNumberItemValidator(),
                        new ClaimCpfCnpjItemValidator(),
                        new ClaimFantasyNameItemValidator(),
                        new ClaimIspbItemValidator(),
                        new ClaimNameItemValidator()
                ));
    }

    @Bean
    public DictItemValidator<Claim> completeClaimItemValidator() {
        return new ClaimValidatorComposite(List.of(
//                new ClaimIdValidator(),
                new ClaimIspbItemValidator()
        ));
    }

    @Bean
    public DictItemValidator listClaimItemValidator() {
        return new ClaimValidatorComposite(
            List.of(
                new ClaimIspbItemValidator()
                   )
        );
    }

    @Bean
    public DictItemValidator<Claim> findClaimItemValidator() {
        return new ClaimValidatorComposite(List.of(
//                new ClaimIdValidator()
        ));
    }

    @Bean
    public DictItemValidator confirmationClaimItemValidator() {
        return new ClaimValidatorComposite(
            List.of(
                new IspbClaimItemValidator()
//                new ClaimIdValidator()
           ));
    }
}
