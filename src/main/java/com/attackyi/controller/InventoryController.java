package com.attackyi.controller;

import com.attackyi.dto.InventoryDtos;
import com.attackyi.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<InventoryDtos.InventoryResponse> upsert(@Valid @RequestBody InventoryDtos.InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.upsert(request));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR') or hasRole('SALES')")
    public ResponseEntity<List<InventoryDtos.InventoryResponse>> list(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.listByProduct(productId));
    }
}
