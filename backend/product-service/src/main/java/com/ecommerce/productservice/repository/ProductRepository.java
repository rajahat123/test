package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByBrand(String brand);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
