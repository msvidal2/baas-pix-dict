package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.*;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "pixKeyJDClient",
        url = "${pix.services.jdpi.dict.url}",
        path = "/jdpi/dict/api")
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
