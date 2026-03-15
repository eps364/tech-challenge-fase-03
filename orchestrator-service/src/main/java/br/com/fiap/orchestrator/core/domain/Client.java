package br.com.fiap.orchestrator.core.domain;

import java.util.UUID;

public class Client {
    private final UUID id;
    private final String cpf;
    private final Address address;

    public Client(UUID id, String cpf, Address address) {
        this.id = id;
        this.cpf = cpf;
        this.address = address;
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
}