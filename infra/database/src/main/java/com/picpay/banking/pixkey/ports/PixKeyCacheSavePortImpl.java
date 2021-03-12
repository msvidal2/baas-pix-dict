package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyCacheSavePort;
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
public class PixKeyCacheSavePortImpl implements PixKeyCacheSavePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(@NonNull PixKey pixKey, @NonNull String requestIdentifier) {
        redisTemplate.opsForHash().put(PixKey.class.getSimpleName(), requestIdentifier, pixKey);
    }
}
