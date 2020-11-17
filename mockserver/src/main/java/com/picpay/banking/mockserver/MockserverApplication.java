package com.picpay.banking.mockserver;

import com.picpay.banking.mockserver.dict.ReconciliationMockServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockserverApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(MockserverApplication.class, args);
        ctx.getBean(ReconciliationMockServer.class).start();
    }

}
