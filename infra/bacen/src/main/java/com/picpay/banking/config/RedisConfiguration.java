/*
 *  baas-pix-dict 1.0 11/23/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author rafael.braga
 * @version 1.0 23/11/2020
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return template;
    }

    /**
     * Sets up a TTL for the keys on Redis.
     * @param keyTtl the Time To Live on all redis keys for this cache
     * @return the RedisCacheConfiguration with TTL
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration(@Value("${spring.redis.keyTtl}") long keyTtl) {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(keyTtl));
    }

}