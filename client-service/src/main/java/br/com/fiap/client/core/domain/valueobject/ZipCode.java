package br.com.fiap.client.core.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

import br.com.fiap.client.core.usecase.ValidationException;

public final class ZipCode {

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");
    private final String value;

    private ZipCode(String value) {
        this.value = value;
    }

    public static ZipCode of(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ValidationException("zipCode", "zipCode is required");
        }
        String normalized = rawValue.trim();
        if (!ZIP_CODE_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("zipCode", "zipCode must match 99999-999 or 99999999");
        }
        return new ZipCode(normalized.replace("-", ""));
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ZipCode zipCode && Objects.equals(value, zipCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
