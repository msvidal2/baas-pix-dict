package com.picpay.banking.pix.infra;

import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.AccountNumberItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.AccountOpeningDateItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.AccountTypeItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.AddressingKeyValidatorComposite;
import com.picpay.banking.pix.core.validators.addressingkey.BranchNumberItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.CpfCnpjItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.FantasyNameItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.IspbItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.KeyItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.KeyTypeItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.NameItemValidator;
import com.picpay.banking.pix.core.validators.addressingkey.PersonTypeItemValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AddressingKeyValidatorConfiguration {

    @Bean
    public DictItemValidator createAddressingKeyItemValidator() {
        return new AddressingKeyValidatorComposite(
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
    public DictItemValidator findAddressingKeyItemValidator() {
        return new AddressingKeyValidatorComposite(
                List.of(
                        new KeyItemValidator()
                ));
    }

    @Bean
    public DictItemValidator removeAddressingKeyItemValidator() {
        return new AddressingKeyValidatorComposite(
                List.of(
                        new KeyTypeItemValidator(),
                        new KeyItemValidator(),
                        new IspbItemValidator()
                ));
    }

    @Bean
    public DictItemValidator updateAddressingKeyItemValidator() {
        return new AddressingKeyValidatorComposite(
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
    public DictItemValidator listAddressingKeyItemValidator() {
        return new AddressingKeyValidatorComposite(
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
