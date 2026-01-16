package com.example.kakeibo.api.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private String category_name;
    private String type_flg;
}