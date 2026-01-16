package com.example.kakeibo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor; // これを追加
import lombok.Setter; // これを追加
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter // 個別のGetter記述を削除可能
@Setter // 個別のSetter記述を削除可能
@NoArgsConstructor // 引数なしのpublicコンストラクタを自動生成
public class Category {

    @Id
    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1)
    private String type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.categoryId == null) {
            this.categoryId = UUID.randomUUID();
        }
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // 明示的な protected Category() {} や Getter/Setter は
    // Lombokアノテーションがあれば削除してOKです。
}