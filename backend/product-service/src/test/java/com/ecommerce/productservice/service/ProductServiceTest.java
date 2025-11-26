package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStatus;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic items");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("PROD-001");
        testProduct.setName("Laptop");
        testProduct.setDescription("High-performance laptop");
        testProduct.setPrice(new BigDecimal("999.99"));
        testProduct.setDiscountPrice(new BigDecimal("899.99"));
        testProduct.setCategory(testCategory);
        testProduct.setStockQuantity(50);
        testProduct.setBrand("TechBrand");
        testProduct.setWeight(new BigDecimal("2.5"));
        testProduct.setStatus(ProductStatus.ACTIVE);
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setUpdatedAt(LocalDateTime.now());

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setSku("PROD-001");
        productDTO.setName("Laptop");
        productDTO.setDescription("High-performance laptop");
        productDTO.setPrice(new BigDecimal("999.99"));
        productDTO.setDiscountPrice(new BigDecimal("899.99"));
        productDTO.setCategoryId(1L);
        productDTO.setStockQuantity(50);
        productDTO.setBrand("TechBrand");
    }

    @Test
    void testCreateProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("PROD-001", result.getSku());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.createProduct(productDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testGetAllProducts() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Mouse");
        product2.setPrice(new BigDecimal("29.99"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct, product2));

        List<ProductDTO> results = productService.getAllProducts();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setName("Updated Laptop");
        updateDTO.setPrice(new BigDecimal("1099.99"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.updateProduct(1L, updateDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetProductsByCategory() {
        when(productRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> results = productService.getProductsByCategory(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(productRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testSearchProducts() {
        when(productRepository.findByNameContainingIgnoreCase("Laptop"))
                .thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> results = productService.searchProducts("Laptop");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Laptop", results.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Laptop");
    }

    @Test
    void testGetProductsByPriceRange() {
        BigDecimal minPrice = new BigDecimal("500.00");
        BigDecimal maxPrice = new BigDecimal("1500.00");

        when(productRepository.findByPriceBetween(minPrice, maxPrice))
                .thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> results = productService.getProductsByPriceRange(minPrice, maxPrice);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(productRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    void testGetProductsByStatus() {
        when(productRepository.findByStatus(ProductStatus.ACTIVE))
                .thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> results = productService.getProductsByStatus(ProductStatus.ACTIVE);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(ProductStatus.ACTIVE, results.get(0).getStatus());
        verify(productRepository, times(1)).findByStatus(ProductStatus.ACTIVE);
    }

    @Test
    void testUpdateProductStatus_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.updateProductStatus(1L, ProductStatus.DISCONTINUED);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
