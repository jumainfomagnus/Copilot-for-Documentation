package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order entity representing customer orders in the e-commerce platform.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(precision = 19, scale = 2, nullable = false)
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Total amount must be a valid decimal number")
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", message = "Subtotal must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Subtotal must be a valid decimal number")
    private BigDecimal subtotal;

    @Column(precision = 19, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", message = "Tax amount must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Tax amount must be a valid decimal number")
    private BigDecimal taxAmount;

    @Column(precision = 19, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", message = "Shipping amount must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Shipping amount must be a valid decimal number")
    private BigDecimal shippingAmount;

    @Column(precision = 19, scale = 2)
    @DecimalMin(value = "0.0", message = "Discount amount must be greater than or equal to 0")
    @Digits(integer = 17, fraction = 2, message = "Discount amount must be a valid decimal number")
    private BigDecimal discountAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    @NotNull(message = "Shipping address is required")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", nullable = false)
    @NotNull(message = "Billing address is required")
    private Address billingAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String paymentTransactionId;

    private LocalDateTime orderDate;

    private LocalDateTime shippedDate;

    private LocalDateTime deliveredDate;

    private String trackingNumber;

    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderStatusHistory> statusHistory;

    /**
     * Order status enumeration
     */
    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED, REFUNDED
    }

    /**
     * Payment method enumeration
     */
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, CASH_ON_DELIVERY
    }

    /**
     * Payment status enumeration
     */
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED, PARTIALLY_REFUNDED
    }

    /**
     * Check if order can be cancelled
     */
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    /**
     * Check if order is completed
     */
    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED;
    }

    /**
     * Calculate total items count
     */
    public int getTotalItemsCount() {
        return orderItems != null ? orderItems.stream().mapToInt(OrderItem::getQuantity).sum() : 0;
    }
}