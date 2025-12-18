package com.attackyi.service;

import com.attackyi.domain.CustomerOrder;
import com.attackyi.domain.OrderItem;
import com.attackyi.domain.Product;
import com.attackyi.domain.UserAccount;
import com.attackyi.dto.OrderDtos;
import com.attackyi.repository.InventoryRepository;
import com.attackyi.repository.OrderRepository;
import com.attackyi.repository.ProductRepository;
import com.attackyi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        UserRepository userRepository, InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public OrderDtos.OrderResponse createOrder(OrderDtos.OrderRequest request) {
        UserAccount user = userRepository.findById(request.getUserId()).orElseThrow();
        CustomerOrder order = new CustomerOrder();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setUser(user);
        order.setStatus("CREATED");
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDtos.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow();
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            order.getItems().add(item);
            total = total.add(itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }
        order.setTotalAmount(total);
        CustomerOrder saved = orderRepository.save(order);
        OrderDtos.OrderResponse response = new OrderDtos.OrderResponse();
        response.setOrderNo(saved.getOrderNo());
        response.setTotalAmount(total);
        response.setStatus(saved.getStatus());
        return response;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> list() {
        return orderRepository.findAll();
    }
}
