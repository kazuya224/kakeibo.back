package com.example.kakeibo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private LocalDate date;

    // '0' = 収入, '1' = 支出
    @Column(nullable = false, length = 1)
    private String type;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 500)
    private String memo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    protected Transaction() {
    }

    public Transaction(UUID transactionId, UUID userId, UUID categoryId,
            LocalDate date, String type, Integer amount, String memo) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.memo = memo;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
