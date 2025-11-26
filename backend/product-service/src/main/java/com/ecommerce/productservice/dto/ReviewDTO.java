package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    private String comment;
    private String userName;
    private Boolean isVerifiedPurchase;
    private String createdAt;
    private String updatedAt;
}
