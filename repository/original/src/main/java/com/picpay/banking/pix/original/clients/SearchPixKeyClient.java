package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.fallbacks.SearchPixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "searchPixKeyClient",
        url = "${pix.services.original.url}",
        path = "/intp-consulta-chave-acesso-java",
        fallbackFactory = SearchPixKeyClientFallbackFactory.class)
public interface SearchPixKeyClient {

    @PostMapping("/v1/access-keys/searches")
    ResponseWrapperDTO<List<ListPixKeyDTO>> listPixKey(@RequestHeader("x-transaction-id") String requestIdentifier, @RequestBody ListPixKeyRequestDTO dto);

}
