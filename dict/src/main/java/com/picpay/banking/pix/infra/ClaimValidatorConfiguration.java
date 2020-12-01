package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimAccountNumberItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimAccountOpeningDateItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimBranchNumberItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimCpfCnpjItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimFantasyNameItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimIdItemValidator;
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
    public DictItemValidator<Claim> claimCancelItemValidator() {
        return new ClaimValidatorComposite(List.of(
                //new ClaimIdItemValidator(),
                //new ClaimIspbItemValidator()
        ));
    }

    @Bean
    public DictItemValidator<Claim> completeClaimItemValidator() {
        return new ClaimValidatorComposite(List.of(
                //new ClaimIdItemValidator(),
                //new ClaimIspbItemValidator()
        ));
    }

    @Bean
    public DictItemValidator confirmationClaimItemValidator() {
        return new ClaimValidatorComposite(
            List.of(
                new IspbClaimItemValidator(),
                new ClaimIdItemValidator()
           ));
    }
}
