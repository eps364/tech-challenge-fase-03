package br.com.fiap.payment.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.payment.core.domain.PaymentStatus;
import br.com.fiap.payment.infra.entity.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByOrderId(UUID orderId);
    List<PaymentEntity> findTop50ByStatusOrderByCreatedAtAsc(PaymentStatus status);
}