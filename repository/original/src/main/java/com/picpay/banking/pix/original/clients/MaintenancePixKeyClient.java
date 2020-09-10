package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyAccountUpdateDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.fallbacks.MaintenancePixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "maintenancePixKeyClient",
        url = "${pix.services.original.url}",
        path = "/intp-manutencao-chave-acesso-java",
        fallbackFactory = MaintenancePixKeyClientFallbackFactory.class)
public interface MaintenancePixKeyClient {

    @PostMapping("/v1/access-keys")
    ResponseWrapperDTO<AccessKeyCreateDTO> create(@RequestBody CreateAccessKeyDTO createAccessKeyDTO);

    @PutMapping("/v1/access-keys")
    ResponseWrapperDTO<AccessKeyAccountUpdateDTO> update(@RequestBody UpdateAccessKeyAccountDTO updateAccessKeyAccountDTO);

}
