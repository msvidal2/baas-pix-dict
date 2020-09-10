package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.jdpi.dto.response.FindPixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.RemovePixKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountPixKeyResponseDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallbackFactory;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "pixKeyJDClient",
        url = "${pix.services.jdpi.dict.url}",
        path = "/jdpi/dict/api",
        fallbackFactory = PixKeyJDClientFallbackFactory.class)
@Headers({
        "Content-Encoding: gzip",
        "Accept-Encoding: gzip"
})
public interface PixKeyJDClient {

    @PostMapping("/v1/incluir")
    CreatePixKeyResponseJDDTO createPixKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                           @RequestBody CreatePixKeyRequestDTO dto);

    @PostMapping("/v1/{key}/excluir")
    RemovePixKeyResponseDTO removeKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                      @PathVariable String key,
                                      @RequestBody RemovePixKeyRequestDTO dto);

    @GetMapping("/v1/{key}")
    FindPixKeyResponseDTO findPixKey(@PathVariable String key,
                                     @RequestHeader("idUsuarioLogado") String userId,
                                     @RequestHeader("PI-EndToEndId") String PIEndToEndId,
                                     @RequestHeader("PI-PayerId") String PIPayerId);

    @PostMapping("/v1/listar/chave")
    ListPixKeyResponseDTO listPixKey(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                     @RequestBody ListPixKeyRequestDTO dto);

    @PutMapping("/v1/{key}")
    UpdateAccountPixKeyResponseDTO updateAccount(@RequestHeader("Chave-Idempotencia") String requestIdentifier,
                                                 @PathVariable String key,
                                                 @RequestBody UpdateAccountPixKeyRequestDTO dto);

}
