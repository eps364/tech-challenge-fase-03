package br.com.fiap.client.core.domain;

import java.util.UUID;

public class Cliente {
    private final UUID id;
    private final String nome;
    private final boolean ativo;

    public Cliente(UUID id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public boolean isAtivo() { return ativo; }

    public Cliente ativar() {
        return new Cliente(this.id, this.nome, true);
    }
}