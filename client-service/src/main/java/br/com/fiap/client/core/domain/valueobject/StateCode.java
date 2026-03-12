package br.com.fiap.client.core.domain.valueobject;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import br.com.fiap.client.core.domain.ValidationException;

public final class StateCode {

    private static final Set<String> VALID_STATES = Set.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );
    private final String value;

    private StateCode(String value) {
        this.value = value;
    }

    public static StateCode of(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ValidationException("state", "The state code is required");
        }
        String normalized = rawValue.trim().toUpperCase(Locale.ROOT);
        if (!VALID_STATES.contains(normalized)) {
            throw new ValidationException("state", "The state code is invalid");
        }
        return new StateCode(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StateCode stateCode && Objects.equals(value, stateCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
