package br.com.fiap.restaurant.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant {
    private final UUID id;
    private final String name;
    private final boolean active;
    private final String street;
    private final String number;
    private final String district;
    private final String complement;
    private final String city;
    private final String state;
    private final String zipCode;
    private final List<UUID> owners;

    public Restaurant(UUID id, String name, boolean active,
                       String street, String number, String district, String complement,
                       String city, String state, String zipCode,
                       List<UUID> owners) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.street = street;
        this.number = number;
        this.district = district;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.owners = owners != null ? new ArrayList<>(owners) : new ArrayList<>();
    }

    public UUID getId()           { return id; }
    public String getName()       { return name; }
    public boolean isActive()      { return active; }
    public String getStreet()     { return street; }
    public String getNumber()     { return number; }
    public String getDistrict()   { return district; }
    public String getComplement() { return complement; }
    public String getCity()       { return city; }
    public String getState()      { return state; }
    public String getZipCode()    { return zipCode; }
    public List<UUID> getOwners() { return owners; }

    public Restaurant activate() {
        return new Restaurant(this.id, this.name, true,
                this.street, this.number, this.district, this.complement,
                this.city, this.state, this.zipCode, this.owners);
    }

    public Restaurant addOwner(UUID ownerId) {
        List<UUID> updated = new ArrayList<>(this.owners);
        if (!updated.contains(ownerId)) {
            updated.add(ownerId);
        }
        return new Restaurant(this.id, this.name, this.active,
                this.street, this.number, this.district, this.complement,
                this.city, this.state, this.zipCode, updated);
    }
}