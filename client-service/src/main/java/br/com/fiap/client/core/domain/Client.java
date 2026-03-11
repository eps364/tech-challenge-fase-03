package br.com.fiap.client.core.domain;

import java.util.UUID;
import java.util.regex.Pattern;

public class Client {
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");

    private final UUID id;
    private final String cpf;
    private final Address address;
    private final boolean active;

    public Client(UUID id, String cpf, Address address, boolean active) {
        if (id == null) {
            throw new IllegalArgumentException("client id is required");
        }
        if (cpf == null || !CPF_PATTERN.matcher(cpf.trim()).matches()) {
            throw new IllegalArgumentException("cpf must have 11 digits");
        }
        if (address == null) {
            throw new IllegalArgumentException("address is required");
        }

        this.id = id;
        this.cpf = cpf.trim();
        this.address = address;
        this.active = active;
    }

    public Client activate() {
        return new Client(id, cpf, address, true);
    }

    public Client withProfile(String newCpf, Address newAddress) {
        return new Client(id, newCpf, newAddress, active);
    }

    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

}