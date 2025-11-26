package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal price;
    
    private BigDecimal discountPrice;
    
    @NotBlank(message = "SKU is required")
    private String sku;
    
    private Long categoryId;
    private String categoryName;
    private String brand;
    private ProductStatus status;
    private String imageUrl;
    private Double averageRating;
    private Integer totalReviews;
    private String createdAt;
    private String updatedAt;
}
