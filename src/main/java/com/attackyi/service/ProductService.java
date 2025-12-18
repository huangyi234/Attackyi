package com.attackyi.service;

import com.attackyi.domain.Product;
import com.attackyi.dto.ProductDtos;
import com.attackyi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDtos.ProductResponse create(ProductDtos.ProductRequest request) {
        Product product = new Product();
        map(request, product);
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductDtos.ProductResponse> list() {
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ProductDtos.ProductResponse update(Long id, ProductDtos.ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow();
        map(request, product);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void toggleActive(Long id, boolean active) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setActive(active);
        productRepository.save(product);
    }

    private void map(ProductDtos.ProductRequest request, Product product) {
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setModel(request.getModel());
        product.setEnergyRating(request.getEnergyRating());
        product.setDimensions(request.getDimensions());
    }

    private ProductDtos.ProductResponse toResponse(Product product) {
        ProductDtos.ProductResponse response = new ProductDtos.ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setModel(product.getModel());
        response.setEnergyRating(product.getEnergyRating());
        response.setDimensions(product.getDimensions());
        response.setActive(product.isActive());
        return response;
    }
}
