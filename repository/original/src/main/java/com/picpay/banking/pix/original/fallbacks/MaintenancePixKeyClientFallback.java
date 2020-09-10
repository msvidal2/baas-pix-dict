package com.picpay.banking.pix.original.fallbacks;

import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;

public class MaintenancePixKeyClientFallback extends ClientFallback implements MaintenancePixKeyClient {

    public MaintenancePixKeyClientFallback(Throwable cause) {
        super(cause);
    }

}
