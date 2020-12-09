package com.picpay.banking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Luis Silva
 * @version 1.0 07/12/2020
 */
@Configuration
@EnableJpaRepositories({
    "com.picpay.banking.pixkey.repository",
    "com.picpay.banking.claim.repository",
    "com.picpay.banking.infraction.repository",
    "com.picpay.banking.reconciliation.repository",
    "com.picpay.banking.config.repository"
})
@EntityScan({
    "com.picpay.banking.pixkey.entity",
    "com.picpay.banking.claim.entity",
    "com.picpay.banking.infraction.entity",
    "com.picpay.banking.reconciliation.entity",
    "com.picpay.banking.config.entity"
})
public class DatabaseConfig {

}
