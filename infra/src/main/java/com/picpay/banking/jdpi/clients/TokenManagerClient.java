package com.picpay.banking.jdpi.clients;

import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.jdpi.interceptors.TokenCacheConfiguration;
import com.picpay.banking.jdpi.interceptors.TokenScope;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tokenManagerClient",
        url = "${pix.services.baas.token-manager.url}")
public interface TokenManagerClient {

    @GetMapping("/v1/token")
    @Cacheable(TokenCacheConfiguration.TOKEN_CACHE_NAME)
    TokenDTO getToken(@RequestParam("scope") TokenScope scope);

}
