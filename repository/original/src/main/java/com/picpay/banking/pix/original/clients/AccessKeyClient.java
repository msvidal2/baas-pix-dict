package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.CreateAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.RemoveAccessKeyDTO;
import com.picpay.banking.pix.original.dto.request.UpdateAccessKeyAccountDTO;
import com.picpay.banking.pix.original.dto.response.*;
import com.picpay.banking.pix.original.fallbacks.AccessKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "accessKeyClient",
        url = "${pix.services.original.url}",
        path = "/dict/v1/access-keys",
        fallbackFactory = AccessKeyClientFallbackFactory.class)
public interface AccessKeyClient {

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
                                                        @RequestParam String key,
                                                        @RequestParam("responsible-key") String responsableKey);

    @GetMapping
    ResponseWrapperDTO<List<ListPixKeyResponseDTO>> listPixKey(@RequestHeader("x-transaction-id") String requestIdentifier,
                                                               @RequestParam("tax-id") String taxId);

}