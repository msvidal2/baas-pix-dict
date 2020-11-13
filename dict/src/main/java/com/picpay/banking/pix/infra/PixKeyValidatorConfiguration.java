package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PixKeyValidatorConfiguration {

    @Bean
    public DictItemValidator createPixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
                        new KeyTypeItemValidator(),
                        new KeyItemValidator(),
                        new IspbItemValidator(),
                        new BranchNumberItemValidator(),
                        new AccountTypeItemValidator(),
                        new AccountNumberItemValidator(),
                        new AccountOpeningDateItemValidator(),
                        new PersonTypeItemValidator(),
                        new CpfCnpjItemValidator(),
                        new NameItemValidator(),
                        new FantasyNameItemValidator()
                ));
    }

    @Bean
    public DictItemValidator findPixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
                        new KeyItemValidator()
                ));
    }

    @Bean
    public DictItemValidator removePixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
                        new KeyTypeItemValidator(),
                        new KeyItemValidator(),
                        new IspbItemValidator()
                ));
    }

    @Bean
    public DictItemValidator updatePixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
                        new KeyItemValidator(),
                        new IspbItemValidator(),
                        new BranchNumberItemValidator(),
                        new AccountTypeItemValidator(),
                        new AccountNumberItemValidator(),
                        new AccountOpeningDateItemValidator()
                ));
    }

    @Bean
    public DictItemValidator listPixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
                        new CpfCnpjItemValidator(),
                        new PersonTypeItemValidator(),
                        new AccountTypeItemValidator(),
                        new AccountNumberItemValidator(),
                        new IspbItemValidator(),
                        new BranchNumberItemValidator()
                ));
    }
}
