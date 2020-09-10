package com.picpay.banking.pix.original.clients;

import com.picpay.banking.pix.original.fallbacks.SearchPixKeyClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "searchPixKeyClient",
        url = "${pix.services.original.url}",
        path = "/intp-consulta-chave-acesso-java",
        fallbackFactory = SearchPixKeyClientFallbackFactory.class)
public interface SearchPixKeyClient {

}
