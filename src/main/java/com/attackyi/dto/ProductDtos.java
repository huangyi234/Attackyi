package com.attackyi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public interface ProductDtos {

    @Data
    class ProductRequest {
        @NotBlank
        private String sku;
        @NotBlank
        private String name;
        private String description;
        @NotNull
        @DecimalMin("0.0")
        private BigDecimal price;
        private String brand;
        private String category;
        private String model;
        private String energyRating;
        private String dimensions;
    }

    @Data
    class ProductResponse {
        private Long id;
        private String sku;
        private String name;
        private String description;
        private BigDecimal price;
        private String brand;
        private String category;
        private String model;
        private String energyRating;
        private String dimensions;
        private boolean active;
    }
}
