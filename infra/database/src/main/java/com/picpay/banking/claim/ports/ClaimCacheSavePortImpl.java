package com.picpay.banking.claim.ports;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimCacheSavePort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 12/03/21
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.redis", value = "enabled", havingValue = "true", matchIfMissing = true)
public class ClaimCacheSavePortImpl implements ClaimCacheSavePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(@NonNull Claim claim, @NonNull String requestIdentifier) {
        redisTemplate.opsForHash().put(Claim.class.getSimpleName(), requestIdentifier, claim);
    }
}
