package br.com.fiap.client.infra.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "address_id")
    private UUID addressId;

    private String street;
    private String number;
    private String neighborhood;
    private String complement;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    protected ClientEntity() {
    }

    public ClientEntity(UUID id, String cpf, boolean active, UUID addressId, String street, String number,
            String neighborhood, String complement, String city, String state, String zipCode) {
        this.id = id;
        this.cpf = cpf;
        this.active = active;
        this.addressId = addressId;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getAddressId() {
        return addressId;
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
