package com.attackyi.repository;

import com.attackyi.domain.InventoryItem;
import com.attackyi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByProduct(Product product);
    Optional<InventoryItem> findByProductIdAndWarehouse(Long productId, String warehouse);
}
