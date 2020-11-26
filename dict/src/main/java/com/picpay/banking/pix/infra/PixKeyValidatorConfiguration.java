package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.validators.*;
import com.picpay.banking.pix.core.validators.pixkey.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PixKeyValidatorConfiguration {

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
//                        new KeyTypeItemValidator(),
//                        new KeyItemValidator(),
//                        new IspbValidator()
                ));
    }

    @Bean
    public DictItemValidator updatePixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
//                        new KeyItemValidator(),
//                        new IspbValidator(),
//                        new BranchNumberItemValidator(),
//                        new AccountTypeValidator(),
//                        new AccountNumberValidator(),
//                        new AccountOpeningDateValidator()
                ));
    }

    @Bean
    public DictItemValidator listPixKeyItemValidator() {
        return new PixKeyValidatorComposite(
                List.of(
//                        new TaxIdValidator(),
//                        new PersonTypeValidator(),
//                        new AccountTypeValidator(),
//                        new AccountNumberValidator(),
//                        new IspbValidator(),
//                        new BranchNumberItemValidator()
                ));
    }
}
