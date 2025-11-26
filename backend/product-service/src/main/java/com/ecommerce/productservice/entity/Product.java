package com.ecommerce.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    private BigDecimal discountPrice;
    
    @Column(nullable = false)
    private String sku;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    private String brand;
    
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    
    private String imageUrl;
    
    private Double averageRating;
    
    private Integer totalReviews;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (averageRating == null) averageRating = 0.0;
        if (totalReviews == null) totalReviews = 0;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
