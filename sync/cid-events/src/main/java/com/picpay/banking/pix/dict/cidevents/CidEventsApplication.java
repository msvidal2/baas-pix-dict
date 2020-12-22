package com.picpay.banking.pix.dict.cidevents;

import com.picpay.banking.reconciliation.config.ReconciliationSyncEventOutputBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class CidEventsApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(CidEventsApplication.class, args);
        var input = ctx.getBean(ReconciliationSyncEventOutputBinding.class);
        input.sendReconciliationSyncEvent().send(new GenericMessage<>("Teste"));
        System.out.println("Teste");
    }

}
