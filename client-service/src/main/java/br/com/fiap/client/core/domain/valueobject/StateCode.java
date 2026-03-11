package br.com.fiap.client.core.domain.valueobject;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import br.com.fiap.client.core.usecase.ValidationException;

public final class StateCode {

    private static final Pattern STATE_PATTERN = Pattern.compile("^[A-Za-z]{2}$");
    private final String value;

    private StateCode(String value) {
        this.value = value;
    }

    public static StateCode of(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ValidationException("state", "state is required");
        }
        String normalized = rawValue.trim();
        if (!STATE_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("state", "state must have 2 letters");
        }
        return new StateCode(normalized.toUpperCase(Locale.ROOT));
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
