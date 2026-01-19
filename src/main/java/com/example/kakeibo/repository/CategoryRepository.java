package com.example.kakeibo.repository;

import com.example.kakeibo.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByUserIdOrderByTypeAscNameAsc(UUID userId);

    // type: '0' or '1'
    List<Category> findByUserIdAndTypeOrderByNameAsc(UUID userId, String type);

    boolean existsByUserIdAndNameAndType(UUID userId, String name, String type);

    List<Category> findByUserIdAndDelFlgOrderByTypeAscNameAsc(UUID userId, String delFlg);
}
