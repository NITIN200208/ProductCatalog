package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.model.Product;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(String id);
    Product updateProduct(String id, Product product);
    void deleteProduct(String id);
    Page<Product> getAllProducts(int page, int size, String category, String brand, String search, String sort);
}
