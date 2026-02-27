package br.com.fiap.restaurantes.core.domain;

import java.util.UUID;

public class Restaurante {
    private final UUID id;
    private final String nome;
    private final boolean ativo;

    public Restaurante(UUID id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public boolean isAtivo() { return ativo; }

    public Restaurante ativar() {
        return new Restaurante(this.id, this.nome, true);
    }
}