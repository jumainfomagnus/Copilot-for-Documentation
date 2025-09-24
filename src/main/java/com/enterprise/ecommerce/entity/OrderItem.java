package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * OrderItem entity representing individual items within an order.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order is required")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Product product;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Column(precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Unit price must be a valid decimal number")
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Total price must be a valid decimal number")
    private BigDecimal totalPrice;

    // Product details at time of order (for historical accuracy)
    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    private String productDescription;

    /**
     * Calculate total price based on quantity and unit price
     */
    @PrePersist
    @PreUpdate
    private void calculateTotalPrice() {
        if (quantity != null && unitPrice != null) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}