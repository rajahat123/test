package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStatus;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPrice(productDTO.getDiscountPrice());
        product.setSku(productDTO.getSku());
        product.setBrand(productDTO.getBrand());
        product.setStatus(productDTO.getStatus() != null ? productDTO.getStatus() : ProductStatus.ACTIVE);
        product.setImageUrl(productDTO.getImageUrl());
        
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }
    
    public ProductDTO getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductDTO> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPrice(productDTO.getDiscountPrice());
        product.setBrand(productDTO.getBrand());
        product.setStatus(productDTO.getStatus());
        product.setImageUrl(productDTO.getImageUrl());
        
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
    
    @Transactional
    public ProductDTO updateProductStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setSku(product.getSku());
        dto.setBrand(product.getBrand());
        dto.setStatus(product.getStatus());
        dto.setImageUrl(product.getImageUrl());
        dto.setAverageRating(product.getAverageRating());
        dto.setTotalReviews(product.getTotalReviews());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        dto.setCreatedAt(product.getCreatedAt() != null ? product.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt().format(formatter) : null);
        
        return dto;
    }
}
