package br.com.fiap.client.infra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.client.infra.entity.ClientEntity;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
}
