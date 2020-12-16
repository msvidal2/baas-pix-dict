package com.picpay.banking.pix.sync;

import com.picpay.banking.pix.sync.eventsourcing.CidProviderConsumer;
import com.picpay.banking.pix.sync.eventsourcing.EventSourceStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableTask
@EnableBinding(value = {CidProviderConsumer.class, EventSourceStream.class})
@EnableFeignClients(basePackages = {"com.picpay.banking.pixkey.clients", "com.picpay.banking.claim.clients", "com.picpay.banking.reconciliation.clients"})
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
@EnableJpaRepositories({"com.picpay.banking.pixkey.repository", "com.picpay.banking.claim.repository", "com.picpay.banking.reconciliation.repository"})
@EntityScan({"com.picpay.banking.pixkey.entity", "com.picpay.banking.claim.entity", "com.picpay.banking.infraction.entity", "com.picpay.banking.reconciliation.entity"})
public class SyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class, args);
    }
}
