package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.fallbacks.MaintenancePixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "maintenancePixKeyClient",
        url = "${pix.services.original.url}",
        path = "/intp-manutencao-chave-acesso-java",
        fallbackFactory = MaintenancePixKeyClientFallbackFactory.class)
public interface MaintenancePixKeyClient {

}
