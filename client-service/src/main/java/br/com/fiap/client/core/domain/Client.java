package br.com.fiap.client.core.domain;

import java.util.UUID;

import br.com.fiap.client.core.domain.valueobject.Cpf;

public final class Client {

    private final UUID id;
    private final String cpf;
    private final Address address;
    private final boolean active;

    public Client(UUID id, String cpf, Address address, boolean active) {
        this(id, Cpf.of(cpf), address, active);
    }

    public Client(UUID id, Cpf cpf, Address address, boolean active) {
        if (id == null) {
            throw new ValidationException("id", "The client id is required");
        }
        if (cpf == null) {
            throw new ValidationException("cpf", "The cpf is required");
        }

        if (address == null) {
            throw new ValidationException("address", "The address is required");
        }

        this.id = id;
        this.cpf = cpf.value();
        this.address = address;
        this.active = active;
    }

    public void ensureCanBeManagedBy(UUID callerId, boolean isAdmin, String message) {
        if (!isAdmin && !id.equals(callerId)) {
            throw new AccessDeniedException(message);
        }
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

    public Cpf getCpfValue() {
        return Cpf.of(cpf);
    }

    public Address getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

}