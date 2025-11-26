package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.CategoryDTO;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.repository.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic items");
        testCategory.setSlug("electronics");
        testCategory.setImageUrl("http://example.com/electronics.jpg");
        testCategory.setActive(true);
        testCategory.setCreatedAt(LocalDateTime.now());
        testCategory.setUpdatedAt(LocalDateTime.now());

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electronics");
        categoryDTO.setDescription("Electronic items");
        categoryDTO.setSlug("electronics");
        categoryDTO.setImageUrl("http://example.com/electronics.jpg");
        categoryDTO.setActive(true);
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        assertEquals("electronics", result.getSlug());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testGetAllCategories() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Clothing");
        category2.setActive(true);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory, category2));

        List<CategoryDTO> results = categoryService.getAllCategories();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory_Success() {
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Updated Electronics");
        updateDTO.setDescription("Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryDTO result = categoryService.updateCategory(1L, updateDTO);

        assertNotNull(result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetActiveCategories() {
        when(categoryRepository.findByActiveTrue()).thenReturn(Arrays.asList(testCategory));

        List<CategoryDTO> results = categoryService.getActiveCategories();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getActive());
        verify(categoryRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testGetSubCategories() {
        Category subCategory = new Category();
        subCategory.setId(2L);
        subCategory.setName("Laptops");
        subCategory.setParent(testCategory);

        when(categoryRepository.findByParentId(1L)).thenReturn(Arrays.asList(subCategory));

        List<CategoryDTO> results = categoryService.getSubCategories(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(categoryRepository, times(1)).findByParentId(1L);
    }
}
