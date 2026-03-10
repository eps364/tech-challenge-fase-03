package br.com.fiap.apigateway.infra.security;

public enum RouteAccessType {
    PERMIT_ALL,
    AUTHENTICATED,
    HAS_ANY_ROLE
}
