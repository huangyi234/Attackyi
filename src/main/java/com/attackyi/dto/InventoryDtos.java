package com.attackyi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public interface InventoryDtos {

    @Data
    class InventoryRequest {
        @NotNull
        private Long productId;
        @NotBlank
        private String warehouse;
        @Min(0)
        private int available;
        @Min(0)
        private int reserved;
        @Min(0)
        private int damaged;
        private String serialNumber;
    }

    @Data
    class InventoryResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String warehouse;
        private int available;
        private int reserved;
        private int damaged;
        private String serialNumber;
    }
}
