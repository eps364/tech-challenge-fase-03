package br.com.fiap.apigateway.infra.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class RouteSecurityRulesStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteSecurityRulesStore.class);
    private static final String RULES_KEY = "gateway:security:rules";
    private static final String OWNER_ROLE = "owner";
    private static final String ADMIN_ROLE = "admin";
    private static final String USER_ROLE = "user";
    private static final String CATALOG_PRODUCTS = "/catalog-service/products";
    private static final String CATALOG_PRODUCTS_WILDCARD = "/catalog-service/products/**";
    private static final TypeReference<List<RouteSecurityRuleDocument>> RULES_DOCUMENT_TYPE = new TypeReference<>() {
    };

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RouteSecurityRulesStore(ReactiveStringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<List<RouteSecurityRule>> getRules() {
        return redisTemplate.opsForValue()
                .get(RULES_KEY)
                .flatMap(this::deserialize)
                .switchIfEmpty(Mono.just(defaultRules()))
                .onErrorResume(ex -> {
                    LOGGER.warn("Failed to load security rules from Redis, using defaults. Cause: {}", ex.getMessage());
                    return Mono.just(defaultRules());
                });
    }

    public Mono<List<RouteSecurityRule>> saveRules(List<RouteSecurityRule> rules) {
        List<RouteSecurityRule> safeRules = rules == null ? Collections.emptyList() : rules;
        List<RouteSecurityRule> normalized = normalizeIds(safeRules);
        return Mono.fromCallable(() -> serialize(normalized))
            .flatMap(json -> redisTemplate.opsForValue().set(RULES_KEY, Objects.requireNonNull(json)).thenReturn(normalized));
    }

    public int nextId(List<RouteSecurityRule> rules) {
        return rules.stream()
                .map(RouteSecurityRule::id)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    private Mono<List<RouteSecurityRule>> deserialize(String raw) {
        return Mono.fromCallable(() -> objectMapper.readValue(raw, RULES_DOCUMENT_TYPE))
                .map(documents -> documents.stream().map(this::toDomain).toList());
    }

    private String serialize(List<RouteSecurityRule> rules) throws Exception {
        List<RouteSecurityRuleDocument> docs = rules.stream().map(this::toDocument).toList();
        return objectMapper.writeValueAsString(docs);
    }

    @SuppressWarnings("null")
    private RouteSecurityRule toDomain(RouteSecurityRuleDocument document) {
        HttpMethod method = document.method() == null || document.method().isBlank()
                ? null
            : HttpMethod.valueOf(Objects.requireNonNull(document.method()).toUpperCase(Locale.ROOT));
        RouteAccessType accessType = RouteAccessType.valueOf(document.access().toUpperCase(Locale.ROOT));
        List<String> roles = document.roles() == null ? List.of() : document.roles();
        return new RouteSecurityRule(document.id(), method, document.pathPattern(), accessType, roles);
    }

    private RouteSecurityRuleDocument toDocument(RouteSecurityRule rule) {
        String method = rule.method() == null ? null : rule.method().name();
        return new RouteSecurityRuleDocument(rule.id(), method, rule.pathPattern(), rule.access().name(), rule.roles());
    }

    private List<RouteSecurityRule> normalizeIds(List<RouteSecurityRule> rules) {
        Set<Integer> usedIds = new HashSet<>();
        int next = 1;

        List<RouteSecurityRule> normalized = new java.util.ArrayList<>(rules.size());
        for (RouteSecurityRule rule : rules) {
            Integer id = rule.id();
            if (id != null && usedIds.add(id)) {
                normalized.add(rule);
                continue;
            }

            while (usedIds.contains(next)) {
                next++;
            }
            int generatedId = next;
            usedIds.add(generatedId);
            next++;

            normalized.add(new RouteSecurityRule(generatedId, rule.method(), rule.pathPattern(), rule.access(), rule.roles()));
        }

        return normalized;
    }

    private List<RouteSecurityRule> defaultRules() {
        return List.of(
                new RouteSecurityRule(1, null, "/auth-service/auth/register", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(2, null, "/auth-service/auth/login", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(3, null, "/auth-service/auth/refresh", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(4, null, "/auth-service/auth/logout", RouteAccessType.PERMIT_ALL, List.of()),

                new RouteSecurityRule(5, null, "/auth-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(6, null, "/order-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(7, null, "/payment-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(8, null, "/restaurant-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(9, null, "/client-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(10, null, "/catalog-service/test/public", RouteAccessType.PERMIT_ALL, List.of()),

                new RouteSecurityRule(11, HttpMethod.GET, CATALOG_PRODUCTS, RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(12, HttpMethod.GET, CATALOG_PRODUCTS_WILDCARD, RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(13, HttpMethod.GET, "/restaurant-service/restaurants", RouteAccessType.PERMIT_ALL, List.of()),
                new RouteSecurityRule(14, HttpMethod.GET, "/restaurant-service/restaurants/**", RouteAccessType.PERMIT_ALL, List.of()),

                new RouteSecurityRule(15, HttpMethod.POST, CATALOG_PRODUCTS, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),
                new RouteSecurityRule(16, HttpMethod.POST, CATALOG_PRODUCTS_WILDCARD, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),
                new RouteSecurityRule(17, HttpMethod.PUT, CATALOG_PRODUCTS, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),
                new RouteSecurityRule(18, HttpMethod.PUT, CATALOG_PRODUCTS_WILDCARD, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),
                new RouteSecurityRule(19, HttpMethod.DELETE, CATALOG_PRODUCTS, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),
                new RouteSecurityRule(20, HttpMethod.DELETE, CATALOG_PRODUCTS_WILDCARD, RouteAccessType.HAS_ANY_ROLE, List.of(OWNER_ROLE, ADMIN_ROLE)),

                new RouteSecurityRule(21, null, "/auth-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),
                new RouteSecurityRule(22, null, "/order-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),
                new RouteSecurityRule(23, null, "/payment-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),
                new RouteSecurityRule(24, null, "/restaurant-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),
                new RouteSecurityRule(25, null, "/client-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),
                new RouteSecurityRule(26, null, "/catalog-service/test/private", RouteAccessType.HAS_ANY_ROLE, List.of(USER_ROLE)),

                new RouteSecurityRule(27, null, "/gateway/security/routes", RouteAccessType.HAS_ANY_ROLE, List.of(ADMIN_ROLE)),
                new RouteSecurityRule(28, null, "/gateway/security/routes/**", RouteAccessType.HAS_ANY_ROLE, List.of(ADMIN_ROLE))
        );
    }

    private record RouteSecurityRuleDocument(
            Integer id,
            String method,
            String pathPattern,
            String access,
            List<String> roles
    ) {
    }
}
