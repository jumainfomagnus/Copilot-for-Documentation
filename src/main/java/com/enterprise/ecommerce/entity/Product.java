package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product entity representing items available for purchase in the e-commerce platform.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;

    @Column(length = 2000)
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    @Column(precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Price must be a valid decimal number")
    private BigDecimal price;

    @Column(precision = 19, scale = 2)
    @DecimalMin(value = "0.0", message = "Cost must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Cost must be a valid decimal number")
    private BigDecimal cost;

    @Column(nullable = false)
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stockQuantity;

    @Min(value = 0, message = "Minimum stock level must be greater than or equal to 0")
    private Integer minimumStockLevel = 10;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean featured = false;

    @DecimalMin(value = "0.0", message = "Weight must be greater than or equal to 0")
    @DecimalMax(value = "999.99", message = "Weight must not exceed 999.99")
    private BigDecimal weight;

    @Size(max = 50, message = "Weight unit must not exceed 50 characters")
    private String weightUnit = "kg";

    @Size(max = 100, message = "Dimensions must not exceed 100 characters")
    private String dimensions;

    @Size(max = 50, message = "Brand must not exceed 50 characters")
    private String brand;

    @Size(max = 100, message = "Model must not exceed 100 characters")
    private String model;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;

    @Size(max = 20, message = "Size must not exceed 20 characters")
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is required")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Column(name = "rating_average", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "sales_count")
    private Long salesCount = 0L;

    /**
     * Product status enumeration
     */
    public enum ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED, PENDING_APPROVAL
    }

    /**
     * Check if product is available for purchase
     */
    public boolean isAvailable() {
        return active && status == ProductStatus.ACTIVE && stockQuantity > 0;
    }

    /**
     * Check if product stock is low
     */
    public boolean isLowStock() {
        return stockQuantity <= minimumStockLevel;
    }
}