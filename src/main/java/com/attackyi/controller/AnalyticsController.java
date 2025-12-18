package com.attackyi.controller;

import com.attackyi.repository.OrderRepository;
import com.attackyi.repository.ProductRepository;
import com.attackyi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AnalyticsController(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> overview() {
        Map<String, Object> metrics = Map.of(
                "orders", orderRepository.count(),
                "products", productRepository.count(),
                "users", userRepository.count());
        return ResponseEntity.ok(metrics);
    }
}
