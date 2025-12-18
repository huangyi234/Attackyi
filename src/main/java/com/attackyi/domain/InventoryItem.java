package com.attackyi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory_items")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 100)
    private String warehouse;

    @Column(nullable = false)
    private Integer available;

    @Column(nullable = false)
    private Integer reserved;

    @Column(nullable = false)
    private Integer damaged;

    @Column(length = 100)
    private String serialNumber;

    private Instant updatedAt = Instant.now();
}
