package com.attackyi.controller;

import com.attackyi.domain.CustomerOrder;
import com.attackyi.dto.OrderDtos;
import com.attackyi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    public ResponseEntity<OrderDtos.OrderResponse> create(@Valid @RequestBody OrderDtos.OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<List<CustomerOrder>> list() {
        return ResponseEntity.ok(orderService.list());
    }
}
