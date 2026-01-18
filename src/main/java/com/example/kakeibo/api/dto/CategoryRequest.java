package com.example.kakeibo.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CategoryRequest {
    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("type_flg")
    private String typeFlg;
}
