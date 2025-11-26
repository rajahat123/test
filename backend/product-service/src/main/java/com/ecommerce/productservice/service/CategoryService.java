package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.CategoryDTO;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIsActive(categoryDTO.getIsActive() != null ? categoryDTO.getIsActive() : true);
        
        if (categoryDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }
        
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }
    
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToDTO(category);
    }
    
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<CategoryDTO> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIsActive(categoryDTO.getIsActive());
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIsActive(category.getIsActive());
        
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }
        
        dto.setCreatedAt(category.getCreatedAt() != null ? category.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(category.getUpdatedAt() != null ? category.getUpdatedAt().format(formatter) : null);
        
        return dto;
    }
}
