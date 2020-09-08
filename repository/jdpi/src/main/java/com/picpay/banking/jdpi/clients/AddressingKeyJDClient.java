package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.CreateAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.RemoveAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.UpdateAccountAddressingKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseJDDTO;
import com.picpay.banking.jdpi.dto.response.FindAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.RemoveAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.fallbacks.AddressingKeyJDClientFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "addressingKey",
        url = "${pix.services.jdpi.dict.url}",
        path = "/jdpi/dict/api",
        fallbackFactory = AddressingKeyJDClientFallbackFactory.class)
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface AddressingKeyJDClient {

    @PostMapping("/v1/incluir")
    CreateAddressingKeyResponseJDDTO createAddressingKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                                         @RequestBody CreateAddressingKeyRequestDTO dto);

    @PostMapping("/v1/{key}/excluir")
    RemoveAddressingKeyResponseDTO removeKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                             @PathVariable String key,
                                             @RequestBody RemoveAddressingKeyRequestDTO dto);

    @GetMapping("/v1/{key}")
    FindAddressingKeyResponseDTO findAddressingKey(@PathVariable String key,
                                                   @RequestHeader("idUsuarioLogado") String userId,
                                                   @RequestHeader("PI-EndToEndId") String PIEndToEndId,
                                                   @RequestHeader("PI-PayerId") String PIPayerId);

    @PostMapping("/v1/listar/chave")
    ListAddressingKeyResponseDTO listAddressingKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                                   @RequestBody ListAddressingKeyRequestDTO dto);

    @PutMapping("/v1/{key}")
    UpdateAccountAddressingKeyResponseDTO updateAccount(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                                        @PathVariable String key,
                                                        @RequestBody UpdateAccountAddressingKeyRequestDTO dto);

}
