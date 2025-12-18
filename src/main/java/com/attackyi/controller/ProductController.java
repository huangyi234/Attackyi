package com.attackyi.controller;

import com.attackyi.dto.ProductDtos;
import com.attackyi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDtos.ProductResponse>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<ProductDtos.ProductResponse> create(@Valid @RequestBody ProductDtos.ProductRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<ProductDtos.ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductDtos.ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @PostMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggle(@PathVariable Long id, @RequestParam boolean active) {
        productService.toggleActive(id, active);
        return ResponseEntity.noContent().build();
    }
}
