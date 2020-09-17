package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.*;
import com.picpay.banking.pix.original.fallbacks.MaintenancePixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "maintenancePixKeyClient",
        url = "${pix.services.original.url}",
        path = "/dict/v1/access-keys",
        fallbackFactory = MaintenancePixKeyClientFallbackFactory.class)
public interface MaintenancePixKeyClient {

    @PostMapping
    ResponseWrapperDTO<AccessKeyCreateDTO> createPixKey(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                        @RequestBody CreateAccessKeyDTO createAccessKeyDTO);

    @PostMapping("/evp")
    ResponseWrapperDTO<AccessKeyCreateDTO> createEvpPixKey(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                           @RequestBody CreateAccessKeyDTO createAccessKeyDTO);

    @PutMapping
    ResponseWrapperDTO<AccessKeyAccountUpdateDTO> update(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                         @RequestBody UpdateAccessKeyAccountDTO updateAccessKeyAccountDTO);

    @DeleteMapping
    ResponseWrapperDTO<AccessKeyRemoveDTO> remove(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                  @RequestBody RemoveAccessKeyDTO removeAccessKeyDTO);

    @GetMapping
    ResponseWrapperDTO<FindPixKeyResponseDTO> findByKey(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                        @PathVariable String key,
                                                        @PathVariable String responsableKey);

    @GetMapping
    ResponseWrapperDTO<List<ListPixKeyResponseDTO>> listPixKey(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                       @PathVariable("tax-id") String taxId);

}