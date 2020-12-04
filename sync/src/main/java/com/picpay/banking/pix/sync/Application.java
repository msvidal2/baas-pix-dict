package com.picpay.banking.pix.sync;

import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import com.picpay.banking.pix.sync.eventsourcing.CidProviderConsumer;
import com.picpay.banking.pix.sync.eventsourcing.EventSourceStream;
import com.picpay.banking.pix.sync.schedulers.SyncVerifierScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableBinding(value = {CidProviderConsumer.class, EventSourceStream.class})
@EnableFeignClients(basePackages = {"com.picpay.banking.jdpi.clients", "com.picpay.banking.pixkey.clients", "com.picpay.banking.claim.clients", "com.picpay.banking.infraction.client", "com.picpay.banking.reconciliation.clients"})
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
@EnableJpaRepositories({"com.picpay.banking.pixkey.repository", "com.picpay.banking.claim.repository", "com.picpay.banking.infraction.ports.picpay", "com.picpay.banking.reconciliation.repository"})
@EntityScan({"com.picpay.banking.pixkey.entity", "com.picpay.banking.claim.entity", "com.picpay.banking.infraction.entity", "com.picpay.banking.reconciliation.entity"})
public class Application {

    public static void main(String[] args) throws Exception {
        var ctx = SpringApplication.run(Application.class, args);
        var syncVerifierScheduler = ctx.getBean(SyncVerifierScheduler.class);
        syncVerifierScheduler.run();
    }

    @Bean
    public static CidProviderUseCase cidProviderUseCase() {
        return new CidProviderUseCase();
    }

}
