package com.picpay.banking.jdpi.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableCaching
@Configuration
@EnableScheduling
public class TokenCacheConfiguration {

    public final static String TOKEN_CACHE_NAME = "dict-token";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(TOKEN_CACHE_NAME);
    }

    @CacheEvict(allEntries = true, value = TOKEN_CACHE_NAME)
    @Scheduled(fixedDelay = 30_000)
    public void removeTokenFromCache() {
        log.info("TokenCacheConfiguration_removeTokenFromCache");
    }

}
