package br.com.fiap.orchestrator.core.domain;

public class Address {
    private final String street;
    private final String number;
    private final String city;
    private final String neighborhood;
    private final String country;
    private final String state;
    private final String zipCode;

    public Address(String street, String number, String city, String neighborhood,
                   String country, String state, String zipCode) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.neighborhood = neighborhood;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
}