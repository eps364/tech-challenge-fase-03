package br.com.fiap.authservice.infra.gateway;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;

@Component
public class RedisTokenBlacklistGatewayImpl implements TokenBlacklistGateway {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    public RedisTokenBlacklistGatewayImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void blacklist(String jti, long ttlSeconds) {
        if (ttlSeconds > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jti, "1", ttlSeconds, TimeUnit.SECONDS);
        }
    }
}
