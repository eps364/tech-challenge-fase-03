package br.com.fiap.client.core.domain;

import java.util.UUID;
import java.util.regex.Pattern;

public class Address {

    private static final Pattern STATE_PATTERN = Pattern.compile("^[A-Za-z]{2}$");
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    private final UUID id;
    private final String street;
    private final String number;
    private final String neighborhood;
    private final String complement;
    private final String city;
    private final String state;
    private final String zipCode;

    public Address(UUID id, String street, String number, String neighborhood, String complement, String city,
            String state, String zipCode) {
        if (id == null) {
            throw new IllegalArgumentException("address id is required");
        }
        if (isBlank(street)) {
            throw new IllegalArgumentException("street is required");
        }
        if (isBlank(number)) {
            throw new IllegalArgumentException("number is required");
        }
        if (isBlank(neighborhood)) {
            throw new IllegalArgumentException("neighborhood is required");
        }
        if (isBlank(city)) {
            throw new IllegalArgumentException("city is required");
        }
        if (isBlank(state) || !STATE_PATTERN.matcher(state).matches()) {
            throw new IllegalArgumentException("state must have 2 letters");
        }
        if (isBlank(zipCode) || !ZIP_CODE_PATTERN.matcher(zipCode).matches()) {
            throw new IllegalArgumentException("zipCode must match 99999-999 or 99999999");
        }

        this.id = id;
        this.street = street.trim();
        this.number = number.trim();
        this.neighborhood = neighborhood.trim();
        this.complement = complement == null ? null : complement.trim();
        this.city = city.trim();
        this.state = state.trim().toUpperCase();
        this.zipCode = zipCode.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

}
