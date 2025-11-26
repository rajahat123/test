package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDTO;
import com.ecommerce.productservice.entity.ProductStatus;
import com.ecommerce.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
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
        productDTO.setStatus(ProductStatus.ACTIVE);
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.sku").value("PROD-001"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void testGetProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetAllProducts_Success() throws Exception {
        List<ProductDTO> products = Arrays.asList(productDTO);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        when(productService.updateProduct(anyLong(), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductDTO.class));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testSearchProducts_Success() throws Exception {
        List<ProductDTO> products = Arrays.asList(productDTO);
        when(productService.searchProducts("Laptop")).thenReturn(products);

        mockMvc.perform(get("/api/products/search")
                .param("keyword", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService, times(1)).searchProducts("Laptop");
    }
}
