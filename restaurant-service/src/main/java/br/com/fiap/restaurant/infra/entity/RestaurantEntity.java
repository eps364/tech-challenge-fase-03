package br.com.fiap.restaurant.infra.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active;

    private String street;
    private String number;
    private String district;
    private String complement;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurant_owners",
            joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "owner_id")
    private Set<UUID> owners = new HashSet<>();

    protected RestaurantEntity() {}

    public RestaurantEntity(UUID id, String name, boolean active,
                            String street, String number, String district, String complement,
                            String city, String state, String zipCode) {
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
    public Set<UUID> getOwners()  { return owners; }
    public void setOwners(Set<UUID> owners) { this.owners = owners; }
}
