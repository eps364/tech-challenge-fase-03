package br.com.fiap.client.core.domain;

import java.util.UUID;

import br.com.fiap.client.core.domain.valueobject.StateCode;
import br.com.fiap.client.core.domain.valueobject.ZipCode;

public class Address {

    private final UUID id;
    private final String street;
    private final String number;
    private final String neighborhood;
    private final String complement;
    private final String city;
    private final StateCode state;
    private final ZipCode zipCode;

    public Address(UUID id, String street, String number, String neighborhood, String complement, String city,
            String state, String zipCode) {
        if (id == null) {
            throw new ValidationException("id", "The address id is required");
        }

        this.id = id;
        this.street = normalizeRequired(street, "street");
        this.number = normalizeRequired(number, "number");
        this.neighborhood = normalizeRequired(neighborhood, "neighborhood");
        this.city = normalizeRequired(city, "city");
        this.complement = complement == null ? null : complement.trim();
        this.state = StateCode.of(state);
        this.zipCode = ZipCode.of(zipCode);
    }

    public Address(UUID id, String street, String number, String neighborhood, String complement, String city,
            StateCode state, ZipCode zipCode) {
        if (id == null) {
            throw new ValidationException("id", "The address id is required");
        }
        this.id = id;
        this.street = normalizeRequired(street, "street");
        this.number = normalizeRequired(number, "number");
        this.neighborhood = normalizeRequired(neighborhood, "neighborhood");
        this.complement = complement == null ? null : complement.trim();
        this.city = normalizeRequired(city, "city");
        this.state = state == null ? StateCode.of(null) : state;
        this.zipCode = zipCode == null ? ZipCode.of(null) : zipCode;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String normalizeRequired(String value, String fieldName) {
        if (isBlank(value)) {
            String field = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            throw new ValidationException(fieldName, "The " + fieldName + " is required");
        }
        return value.trim();
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

    public StateCode getStateCode() {
        return state;
    }

    public String getState() {
        return state.value();
    }

    public ZipCode getZipCodeValue() {
        return zipCode;
    }

    public String getZipCode() {
        return zipCode.value();
    }

}
