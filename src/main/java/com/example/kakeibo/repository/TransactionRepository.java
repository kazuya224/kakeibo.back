package com.example.kakeibo.repository;

import com.example.kakeibo.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByDateBetweenOrderByDateAscTransactionIdAsc(LocalDate from, LocalDate to);

    List<Transaction> findByDateOrderByTransactionIdAsc(LocalDate date);

    List<Transaction> findByUserIdOrderByDateDesc(UUID userId);

    Optional<Transaction> findByTransactionIdAndUserId(UUID transactionId, UUID userId);

    void deleteByTransactionIdAndUserId(UUID transactionId, UUID userId);

    boolean existsByTransactionIdAndUserId(UUID transactionId, UUID userId);
}
