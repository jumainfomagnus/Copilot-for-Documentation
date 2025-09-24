package com.enterprise.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for OrderItem entity.
 */
@Data
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productSku;
    private String productDescription;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Unit price must be a valid decimal number")
    private BigDecimal unitPrice;
    
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Total price must be a valid decimal number")
    private BigDecimal totalPrice;
}