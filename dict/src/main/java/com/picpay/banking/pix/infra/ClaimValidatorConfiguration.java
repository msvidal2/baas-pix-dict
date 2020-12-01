package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ClaimValidatorConfiguration {


    @Bean
    public DictItemValidator<Claim> completeClaimItemValidator() {
        return new ClaimValidatorComposite(List.of(
//                new ClaimIdValidator(),
//                new ClaimIspbItemValidator()
        ));
    }

    @Bean
    public DictItemValidator listClaimItemValidator() {
        return new ClaimValidatorComposite(
                List.of(
//                new ClaimIspbItemValidator()
                ));
    }

    @Bean
    public DictItemValidator<Claim> findClaimItemValidator() {
        return new ClaimValidatorComposite(List.of(
//                new ClaimIdValidator()
        ));
    }

}
