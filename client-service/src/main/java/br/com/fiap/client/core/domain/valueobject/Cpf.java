package br.com.fiap.client.core.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

import br.com.fiap.client.core.usecase.ValidationException;

public final class Cpf {

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private final String value;

    private Cpf(String value) {
        this.value = value;
    }

    public static Cpf of(String rawValue) {
        if (rawValue == null) {
            throw new ValidationException("cpf", "cpf is required");
        }
        String normalized = rawValue.trim();
        if (!CPF_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("cpf", "cpf must have 11 digits");
        }
        return new Cpf(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Cpf cpf && Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
