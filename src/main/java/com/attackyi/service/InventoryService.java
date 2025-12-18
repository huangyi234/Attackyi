package com.attackyi.service;

import com.attackyi.domain.InventoryItem;
import com.attackyi.domain.Product;
import com.attackyi.dto.InventoryDtos;
import com.attackyi.repository.InventoryRepository;
import com.attackyi.repository.ProductRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository, StringRedisTemplate redisTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public InventoryDtos.InventoryResponse upsert(InventoryDtos.InventoryRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow();
        InventoryItem item = inventoryRepository.findByProductIdAndWarehouse(request.getProductId(), request.getWarehouse())
                .orElseGet(InventoryItem::new);
        item.setProduct(product);
        item.setWarehouse(request.getWarehouse());
        item.setAvailable(request.getAvailable());
        item.setReserved(request.getReserved());
        item.setDamaged(request.getDamaged());
        item.setSerialNumber(request.getSerialNumber());
        InventoryItem saved = inventoryRepository.save(item);
        cacheStock(saved);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<InventoryDtos.InventoryResponse> listByProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return inventoryRepository.findByProduct(product).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private void cacheStock(InventoryItem item) {
        String key = "stock:" + item.getProduct().getSku();
        int salable = Math.max(0, item.getAvailable() - item.getReserved() - item.getDamaged());
        redisTemplate.opsForValue().set(key, String.valueOf(salable));
    }

    private InventoryDtos.InventoryResponse toResponse(InventoryItem item) {
        InventoryDtos.InventoryResponse response = new InventoryDtos.InventoryResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setWarehouse(item.getWarehouse());
        response.setAvailable(item.getAvailable());
        response.setReserved(item.getReserved());
        response.setDamaged(item.getDamaged());
        response.setSerialNumber(item.getSerialNumber());
        return response;
    }
}
