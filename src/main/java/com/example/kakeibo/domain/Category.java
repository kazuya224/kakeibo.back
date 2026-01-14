package com.example.kakeibo.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    // '0' = 収入, '1' = 支出
    @Column(nullable = false, length = 1)
    private String type;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Category() {
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
