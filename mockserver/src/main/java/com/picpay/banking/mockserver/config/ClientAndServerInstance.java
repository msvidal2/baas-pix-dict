package com.picpay.banking.mockserver.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockserver.integration.ClientAndServer;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientAndServerInstance {

    private static ClientAndServer instance;

    public static ClientAndServer get() {
        if (instance == null) instance = new ClientAndServer(1080);
        return instance;
    }

}
