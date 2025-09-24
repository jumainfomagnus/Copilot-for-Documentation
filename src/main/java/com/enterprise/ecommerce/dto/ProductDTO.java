package com.enterprise.ecommerce.dto;

import com.enterprise.ecommerce.entity.Product;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Product entity.
 */
@Data
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Cost must be greater than or equal to 0")
    private BigDecimal cost;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stockQuantity;
    
    @Min(value = 0, message = "Minimum stock level must be greater than or equal to 0")
    private Integer minimumStockLevel;
    
    private Boolean active;
    private Boolean featured;
    private BigDecimal weight;
    private String weightUnit;
    private String dimensions;
    private String brand;
    private String model;
    private String color;
    private String size;
    private Product.ProductStatus status;
    private Long categoryId;
    private String categoryName;
    private List<ProductImageDTO> images;
    private BigDecimal averageRating;
    private Integer ratingCount;
    private Long viewCount;
    private Long salesCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /**
     * Request DTO for product creation
     */
    @Data
    public static class CreateProductRequest {
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must not exceed 255 characters")
        private String name;
        
        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        private String description;
        
        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU must not exceed 100 characters")
        private String sku;
        
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        private BigDecimal price;
        
        @DecimalMin(value = "0.0", message = "Cost must be greater than or equal to 0")
        private BigDecimal cost;
        
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
        private Integer stockQuantity;
        
        @Min(value = 0, message = "Minimum stock level must be greater than or equal to 0")
        private Integer minimumStockLevel = 10;
        
        private Boolean active = true;
        private Boolean featured = false;
        private BigDecimal weight;
        private String weightUnit = "kg";
        private String dimensions;
        private String brand;
        private String model;
        private String color;
        private String size;
        
        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }

    /**
     * Request DTO for product update
     */
    @Data
    public static class UpdateProductRequest {
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must not exceed 255 characters")
        private String name;
        
        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        private String description;
        
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        private BigDecimal price;
        
        @DecimalMin(value = "0.0", message = "Cost must be greater than or equal to 0")
        private BigDecimal cost;
        
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
        private Integer stockQuantity;
        
        @Min(value = 0, message = "Minimum stock level must be greater than or equal to 0")
        private Integer minimumStockLevel;
        
        private Boolean active;
        private Boolean featured;
        private BigDecimal weight;
        private String weightUnit;
        private String dimensions;
        private String brand;
        private String model;
        private String color;
        private String size;
        private Product.ProductStatus status;
        
        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }
}