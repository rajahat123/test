package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ReviewDTO;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.Review;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review testReview;
    private Product testProduct;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop");

        testReview = new Review();
        testReview.setId(1L);
        testReview.setProduct(testProduct);
        testReview.setUserId(1L);
        testReview.setRating(5);
        testReview.setTitle("Great Product");
        testReview.setComment("Excellent laptop, highly recommended!");
        testReview.setVerifiedPurchase(true);
        testReview.setCreatedAt(LocalDateTime.now());
        testReview.setUpdatedAt(LocalDateTime.now());

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setProductId(1L);
        reviewDTO.setUserId(1L);
        reviewDTO.setRating(5);
        reviewDTO.setTitle("Great Product");
        reviewDTO.setComment("Excellent laptop, highly recommended!");
        reviewDTO.setVerifiedPurchase(true);
    }

    @Test
    void testCreateReview_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewDTO result = reviewService.createReview(reviewDTO);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Great Product", result.getTitle());
        verify(productRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testCreateReview_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.createReview(reviewDTO));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testGetReviewById_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        ReviewDTO result = reviewService.getReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getRating());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReviewById_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.getReviewById(1L));
    }

    @Test
    void testGetReviewsByProductId() {
        Review review2 = new Review();
        review2.setId(2L);
        review2.setProduct(testProduct);
        review2.setRating(4);

        when(reviewRepository.findByProductId(1L)).thenReturn(Arrays.asList(testReview, review2));

        List<ReviewDTO> results = reviewService.getReviewsByProductId(1L);

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(reviewRepository, times(1)).findByProductId(1L);
    }

    @Test
    void testUpdateReview_Success() {
        ReviewDTO updateDTO = new ReviewDTO();
        updateDTO.setRating(4);
        updateDTO.setTitle("Updated Review");
        updateDTO.setComment("Still good but with minor issues");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewDTO result = reviewService.updateReview(1L, updateDTO);

        assertNotNull(result);
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).existsById(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> reviewService.deleteReview(1L));
        verify(reviewRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetReviewsByUserId() {
        when(reviewRepository.findByUserId(1L)).thenReturn(Arrays.asList(testReview));

        List<ReviewDTO> results = reviewService.getReviewsByUserId(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getUserId());
        verify(reviewRepository, times(1)).findByUserId(1L);
    }
}
