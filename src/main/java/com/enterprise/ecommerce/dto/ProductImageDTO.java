package com.enterprise.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for ProductImage entity.
 */
@Data
public class ProductImageDTO {
    private Long id;
    
    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
    
    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;
    
    private Boolean isPrimary;
    private Integer sortOrder;
    private Boolean active;
}