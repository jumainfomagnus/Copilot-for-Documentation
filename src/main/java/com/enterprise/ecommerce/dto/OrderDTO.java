package com.enterprise.ecommerce.dto;

import com.enterprise.ecommerce.entity.Order;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Order entity.
 */
@Data
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userFullName;
    private Order.OrderStatus status;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Total amount must be a valid decimal number")
    private BigDecimal totalAmount;
    
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    
    private List<OrderItemDTO> orderItems;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    
    private Order.PaymentMethod paymentMethod;
    private Order.PaymentStatus paymentStatus;
    private String paymentTransactionId;
    
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private String trackingNumber;
    private String notes;
    
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /**
     * Request DTO for order creation
     */
    @Data
    public static class CreateOrderRequest {
        @NotNull(message = "Shipping address ID is required")
        private Long shippingAddressId;
        
        @NotNull(message = "Billing address ID is required")
        private Long billingAddressId;
        
        @NotNull(message = "Payment method is required")
        private Order.PaymentMethod paymentMethod;
        
        private String notes;
    }

    /**
     * Request DTO for order status update
     */
    @Data
    public static class UpdateOrderStatusRequest {
        @NotNull(message = "Status is required")
        private Order.OrderStatus status;
        
        private String notes;
        private String trackingNumber;
    }
}