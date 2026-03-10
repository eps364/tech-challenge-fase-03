package br.com.fiap.apigateway.infra.web.controller;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.apigateway.core.dto.RouteSecurityRuleRequest;
import br.com.fiap.apigateway.core.dto.RouteSecurityRuleResponse;
import br.com.fiap.apigateway.infra.security.RouteAccessType;
import br.com.fiap.apigateway.infra.security.RouteSecurityRule;
import br.com.fiap.apigateway.infra.security.RouteSecurityRulesStore;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway/security/routes")
public class RouteSecurityRulesController {

    private final RouteSecurityRulesStore rulesStore;

    public RouteSecurityRulesController(RouteSecurityRulesStore rulesStore) {
        this.rulesStore = rulesStore;
    }

    @GetMapping
    public Mono<List<RouteSecurityRuleResponse>> findAll() {
        return rulesStore.getRules()
                .map(rules -> rules.stream().map(this::toResponse).toList());
    }

    @PutMapping
    public Mono<List<RouteSecurityRuleResponse>> update(@RequestBody List<RouteSecurityRuleRequest> requests) {
        List<RouteSecurityRule> rules = requests == null
                ? List.of()
                : requests.stream().map(this::toDomain).toList();

        return rulesStore.saveRules(rules)
                .map(saved -> saved.stream().map(this::toResponse).toList());
    }

    @PostMapping
    public Mono<RouteSecurityRuleResponse> create(@RequestBody RouteSecurityRuleRequest request) {
        RouteSecurityRule ruleWithoutId = toDomain(request, null);
        return rulesStore.getRules()
                .flatMap(existing -> {
                    int nextId = rulesStore.nextId(existing);
                    RouteSecurityRule created = withId(ruleWithoutId, nextId);
                    List<RouteSecurityRule> updated = new java.util.ArrayList<>(existing);
                    updated.add(created);
                    return rulesStore.saveRules(updated).thenReturn(created);
                })
                .map(this::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<RouteSecurityRuleResponse> updateById(
            @PathVariable Integer id,
            @RequestBody RouteSecurityRuleRequest request
    ) {
        RouteSecurityRule incoming = toDomain(request, id);
        return rulesStore.getRules()
                .flatMap(existing -> {
                    List<RouteSecurityRule> updated = new java.util.ArrayList<>(existing);
                    int index = findIndexById(updated, id)
                            .orElseThrow(() -> notFound(id));
                    updated.set(index, incoming);
                    return rulesStore.saveRules(updated).thenReturn(incoming);
                })
                .map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable Integer id) {
        return rulesStore.getRules()
                .flatMap(existing -> {
                    List<RouteSecurityRule> updated = new java.util.ArrayList<>(existing);
                    int index = findIndexById(updated, id)
                            .orElseThrow(() -> notFound(id));
                    updated.remove(index);
                    return rulesStore.saveRules(updated).then();
                });
    }

    private RouteSecurityRule toDomain(RouteSecurityRuleRequest request) {
        return toDomain(request, request.id());
    }

    private RouteSecurityRule toDomain(RouteSecurityRuleRequest request, Integer id) {
        if (request.pathPattern() == null || request.pathPattern().isBlank()) {
            throw badRequest("pathPattern is required");
        }
        if (request.access() == null || request.access().isBlank()) {
            throw badRequest("access is required");
        }

        HttpMethod method = parseHttpMethod(request.method());
        RouteAccessType access = parseAccessType(request.access());
        List<String> roles = request.roles() == null ? List.of() : request.roles();

        return new RouteSecurityRule(id, method, request.pathPattern(), access, roles);
    }

    private RouteSecurityRuleResponse toResponse(RouteSecurityRule rule) {
        String method = rule.method() == null ? null : rule.method().name();
        return new RouteSecurityRuleResponse(rule.id(), method, rule.pathPattern(), rule.access().name(), rule.roles());
    }

    private RouteSecurityRule withId(RouteSecurityRule rule, Integer id) {
        return new RouteSecurityRule(id, rule.method(), rule.pathPattern(), rule.access(), rule.roles());
    }

    private Optional<Integer> findIndexById(List<RouteSecurityRule> rules, Integer id) {
        for (int i = 0; i < rules.size(); i++) {
            RouteSecurityRule rule = rules.get(i);
            if (Objects.equals(rule.id(), id)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("null")
    private HttpMethod parseHttpMethod(String method) {
        if (method == null || method.isBlank()) {
            return null;
        }
        try {
            return HttpMethod.valueOf(Objects.requireNonNull(method).toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw badRequest("method is invalid. Use standard HTTP method names (GET, POST, PUT, DELETE, PATCH)");
        }
    }

    private RouteAccessType parseAccessType(String access) {
        try {
            return RouteAccessType.valueOf(access.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw badRequest("access is invalid. Use PERMIT_ALL, AUTHENTICATED or HAS_ANY_ROLE");
        }
    }

    private ResponseStatusException badRequest(String detail) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, detail);
    }

    private ResponseStatusException notFound(Integer id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "route rule not found: id=" + id);
    }
}