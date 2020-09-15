package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyAccountUpdateDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyCreateDTO;
import com.picpay.banking.pix.original.dto.response.AccessKeyRemoveDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.fallbacks.MaintenancePixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "maintenancePixKeyClient",
        url = "${pix.services.original.url}",
        path = "/intp-manutencao-chave-acesso-java",
        fallbackFactory = MaintenancePixKeyClientFallbackFactory.class)
public interface MaintenancePixKeyClient {

    @PostMapping("/v1/access-keys")
    ResponseWrapperDTO<AccessKeyCreateDTO> create(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                  @RequestBody CreateAccessKeyDTO createAccessKeyDTO);

    @PutMapping("/v1/access-keys")
    ResponseWrapperDTO<AccessKeyAccountUpdateDTO> update(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                         @RequestBody UpdateAccessKeyAccountDTO updateAccessKeyAccountDTO);

    @DeleteMapping("/v1/access-keys")
    ResponseWrapperDTO<AccessKeyRemoveDTO> remove(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                  @RequestBody RemoveAccessKeyDTO removeAccessKeyDTO);



}
