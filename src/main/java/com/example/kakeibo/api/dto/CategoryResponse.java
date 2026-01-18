package com.example.kakeibo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryInfo> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private String categoryId; // ログの "categoryId"
        private String name; // ログ Robbins "name"
        private String type; // ログの "type" ("0" or "1")
    }
}
