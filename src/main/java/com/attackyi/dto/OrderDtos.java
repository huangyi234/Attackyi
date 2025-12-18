package com.attackyi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDtos {

    @Data
    class OrderItemRequest {
        @NotNull
        private Long productId;
        @NotNull
        private Integer quantity;
        @NotNull
        private BigDecimal unitPrice;
    }

    @Data
    class OrderRequest {
        @NotNull
        private Long userId;
        @NotEmpty
        @Valid
        private List<OrderItemRequest> items;
    }

    @Data
    class OrderResponse {
        private String orderNo;
        private BigDecimal totalAmount;
        private String status;
    }
}
