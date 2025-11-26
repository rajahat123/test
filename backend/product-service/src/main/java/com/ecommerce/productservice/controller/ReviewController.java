package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ReviewDTO;
import com.ecommerce.productservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    // API 33: Create review
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO review = reviewService.createReview(reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
    
    // API 34: Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
    
    // API 35: Get reviews by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }
    
    // API 36: Get reviews by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
    
    // API 37: Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
