package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ReviewDTO;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.Review;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Product product = productRepository.findById(reviewDTO.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Review review = new Review();
        review.setProduct(product);
        review.setUserId(reviewDTO.getUserId());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setUserName(reviewDTO.getUserName());
        review.setIsVerifiedPurchase(reviewDTO.getIsVerifiedPurchase());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update product rating
        updateProductRating(product.getId());
        
        return convertToDTO(savedReview);
    }
    
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDTO(review);
    }
    
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        Long productId = review.getProduct().getId();
        reviewRepository.delete(review);
        updateProductRating(productId);
    }
    
    @Transactional
    private void updateProductRating(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        List<Review> reviews = reviewRepository.findByProductId(productId);
        
        if (reviews.isEmpty()) {
            product.setAverageRating(0.0);
            product.setTotalReviews(0);
        } else {
            double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
            product.setAverageRating(avgRating);
            product.setTotalReviews(reviews.size());
        }
        
        productRepository.save(product);
    }
    
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUserId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setUserName(review.getUserName());
        dto.setIsVerifiedPurchase(review.getIsVerifiedPurchase());
        dto.setCreatedAt(review.getCreatedAt() != null ? review.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(review.getUpdatedAt() != null ? review.getUpdatedAt().format(formatter) : null);
        return dto;
    }
}
