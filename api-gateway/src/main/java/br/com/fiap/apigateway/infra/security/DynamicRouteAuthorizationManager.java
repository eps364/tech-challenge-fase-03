package br.com.fiap.apigateway.infra.security;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.http.server.PathContainer;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import reactor.core.publisher.Mono;

@Component
public class DynamicRouteAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RouteSecurityRulesStore rulesStore;
    private final AntPathMatcher pathMatcher;

    public DynamicRouteAuthorizationManager(RouteSecurityRulesStore rulesStore) {
        this.rulesStore = rulesStore;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        String method = context.getExchange().getRequest().getMethod() == null
                ? null
                : context.getExchange().getRequest().getMethod().name();
        PathContainer path = context.getExchange().getRequest().getPath().pathWithinApplication();
        String requestPath = path.value();

        return rulesStore.getRules()
                .map(rules -> findMatchingRule(rules, method, requestPath))
                .flatMap(matchingRule -> evaluateRule(matchingRule, authentication));
    }

    @SuppressWarnings("null")
    private RouteSecurityRule findMatchingRule(List<RouteSecurityRule> rules, String method, String requestPath) {
        return rules.stream()
                .filter(rule -> matchesMethod(rule, method))
            .filter(rule -> rule.pathPattern() != null && pathMatcher.match(Objects.requireNonNull(rule.pathPattern()), requestPath))
                .findFirst()
                .orElseGet(() -> new RouteSecurityRule(null, null, "**", RouteAccessType.AUTHENTICATED, List.of()));
    }

    private boolean matchesMethod(RouteSecurityRule rule, String method) {
        if (rule.method() == null) {
            return true;
        }
        return method != null && rule.method().name().equalsIgnoreCase(method);
    }

    private Mono<AuthorizationDecision> evaluateRule(RouteSecurityRule rule, Mono<Authentication> authentication) {
        if (rule.access() == RouteAccessType.PERMIT_ALL) {
            return Mono.just(new AuthorizationDecision(true));
        }

        return authentication
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    if (rule.access() == RouteAccessType.AUTHENTICATED) {
                        return true;
                    }
                    return hasAnyRole(auth, rule.roles());
                })
                .map(granted -> new AuthorizationDecision(Boolean.TRUE.equals(granted)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private boolean hasAnyRole(Authentication authentication, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> roles.stream()
                        .map(role -> "ROLE_" + role.toLowerCase(Locale.ROOT))
                        .anyMatch(authority::equalsIgnoreCase));
    }
}
