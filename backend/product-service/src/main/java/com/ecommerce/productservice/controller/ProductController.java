package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.entity.ProductStatus;
import com.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    // API 16: Create product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    // API 17: Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    // API 18: Get product by SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        ProductDTO product = productService.getProductBySku(sku);
        return ResponseEntity.ok(product);
    }
    
    // API 19: Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    // API 20: Get products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    // API 21: Search products
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
    
    // API 22: Get products by brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductDTO>> getProductsByBrand(@PathVariable String brand) {
        List<ProductDTO> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }
    
    // API 23: Get products by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<ProductDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    // API 24: Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(product);
    }
    
    // API 25: Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    // API 26: Update product status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductDTO> updateProductStatus(@PathVariable Long id, @RequestParam ProductStatus status) {
        ProductDTO product = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(product);
    }
}
