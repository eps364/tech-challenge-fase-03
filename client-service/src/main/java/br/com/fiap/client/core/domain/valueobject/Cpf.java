package br.com.fiap.client.core.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

import br.com.fiap.client.core.domain.ValidationException;

public final class Cpf {

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern REPEATED_DIGITS_PATTERN = Pattern.compile("^(\\d)\\1{10}$");
    private final String value;

    private Cpf(String value) {
        this.value = value;
    }

    public static Cpf of(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new ValidationException("cpf", "The cpf is required");
        }
        String normalized = rawValue.trim();
        if (!CPF_PATTERN.matcher(normalized).matches()) {
            throw new ValidationException("cpf", "The cpf is invalid, must have 11 digits");
        }
        if (REPEATED_DIGITS_PATTERN.matcher(normalized).matches() || !hasValidCheckDigits(normalized)) {
            throw new ValidationException("cpf", "The cpf is invalid");
        }
        return new Cpf(normalized);
    }

    private static boolean hasValidCheckDigits(String cpf) {
        int firstDigit = calculateCheckDigit(cpf, 9, 10);
        int secondDigit = calculateCheckDigit(cpf, 10, 11);
        return firstDigit == Character.getNumericValue(cpf.charAt(9))
                && secondDigit == Character.getNumericValue(cpf.charAt(10));
    }

    private static int calculateCheckDigit(String cpf, int length, int weightStart) {
        int sum = 0;
        int weight = weightStart;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * weight;
            weight--;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
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
