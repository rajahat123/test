package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.CategoryDTO;
import com.ecommerce.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    // API 27: Create category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    
    // API 28: Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    // API 29: Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    // API 30: Get subcategories
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryDTO>> getSubCategories(@PathVariable Long parentId) {
        List<CategoryDTO> categories = categoryService.getSubCategories(parentId);
        return ResponseEntity.ok(categories);
    }
    
    // API 31: Update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(category);
    }
    
    // API 32: Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
