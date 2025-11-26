package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic items");
        testCategory.setSlug("electronics");
        testCategory.setActive(true);
        testCategory.setCreatedAt(LocalDateTime.now());
        testCategory.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(testCategory);

        testProduct = new Product();
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
    }

    @Test
    void testFindBySku_Success() {
        entityManager.persist(testProduct);
        entityManager.flush();

        Optional<Product> found = productRepository.findBySku("PROD-001");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Laptop");
    }

    @Test
    void testFindByCategoryId() {
        entityManager.persist(testProduct);
        entityManager.flush();

        List<Product> products = productRepository.findByCategoryId(testCategory.getId());

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getCategory().getId()).isEqualTo(testCategory.getId());
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        entityManager.persist(testProduct);
        entityManager.flush();

        List<Product> products = productRepository.findByNameContainingIgnoreCase("laptop");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).containsIgnoringCase("laptop");
    }

    @Test
    void testFindByPriceBetween() {
        entityManager.persist(testProduct);
        entityManager.flush();

        List<Product> products = productRepository.findByPriceBetween(
                new BigDecimal("500.00"), 
                new BigDecimal("1500.00")
        );

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getPrice()).isBetween(
                new BigDecimal("500.00"), 
                new BigDecimal("1500.00")
        );
    }

    @Test
    void testFindByStatus() {
        entityManager.persist(testProduct);
        entityManager.flush();

        List<Product> products = productRepository.findByStatus(ProductStatus.ACTIVE);

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void testFindByBrand() {
        entityManager.persist(testProduct);
        entityManager.flush();

        List<Product> products = productRepository.findByBrand("TechBrand");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getBrand()).isEqualTo("TechBrand");
    }

    @Test
    void testSaveProduct() {
        Product saved = productRepository.save(testProduct);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSku()).isEqualTo("PROD-001");
    }

    @Test
    void testUpdateProduct() {
        entityManager.persist(testProduct);
        entityManager.flush();

        testProduct.setPrice(new BigDecimal("1099.99"));
        Product updated = productRepository.save(testProduct);

        assertThat(updated.getPrice()).isEqualTo(new BigDecimal("1099.99"));
    }

    @Test
    void testDeleteProduct() {
        Product saved = entityManager.persist(testProduct);
        entityManager.flush();

        productRepository.deleteById(saved.getId());

        Optional<Product> deleted = productRepository.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }
}
