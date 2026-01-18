package com.example.kakeibo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryAddResponse {
    private String category_id;
    private String category_name;
    private String type_flg;
}