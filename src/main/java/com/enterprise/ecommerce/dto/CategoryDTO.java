package com.enterprise.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Category entity.
 */
@Data
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Slug is required")
    @Size(max = 100, message = "Slug must not exceed 100 characters")
    private String slug;
    
    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String imageUrl;
    
    private Boolean active;
    private Integer sortOrder;
    private Long parentCategoryId;
    private String parentCategoryName;
    private List<CategoryDTO> subCategories;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /**
     * Request DTO for category creation
     */
    @Data
    public static class CreateCategoryRequest {
        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        private String name;
        
        @Size(max = 500, message = "Description must not exceed 500 characters")
        private String description;
        
        @NotBlank(message = "Slug is required")
        @Size(max = 100, message = "Slug must not exceed 100 characters")
        private String slug;
        
        @Size(max = 255, message = "Image URL must not exceed 255 characters")
        private String imageUrl;
        
        private Boolean active = true;
        private Integer sortOrder = 0;
        private Long parentCategoryId;
    }
}