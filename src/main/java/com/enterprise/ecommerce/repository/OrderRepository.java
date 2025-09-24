package com.enterprise.ecommerce.repository;

import com.enterprise.ecommerce.entity.Order;
import com.enterprise.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity providing data access methods.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by user
     */
    List<Order> findByUserOrderByOrderDateDesc(User user);

    /**
     * Find orders by user with pagination
     */
    Page<Order> findByUserOrderByOrderDateDesc(User user, Pageable pageable);

    /**
     * Find orders by status
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * Find orders by payment status
     */
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);

    /**
     * Find orders within date range
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Find orders by total amount range
     */
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * Find orders by tracking number
     */
    Optional<Order> findByTrackingNumber(String trackingNumber);

    /**
     * Find recent orders
     */
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    Page<Order> findRecentOrders(Pageable pageable);

    /**
     * Find pending orders
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING', 'CONFIRMED') ORDER BY o.orderDate ASC")
    List<Order> findPendingOrders();

    /**
     * Find orders ready to ship
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PROCESSING' AND o.paymentStatus = 'COMPLETED'")
    List<Order> findOrdersReadyToShip();

    /**
     * Calculate total sales for date range
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'DELIVERED' AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalSales(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);

    /**
     * Count orders by status
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    /**
     * Count orders by payment status
     */
    @Query("SELECT o.paymentStatus, COUNT(o) FROM Order o GROUP BY o.paymentStatus")
    List<Object[]> countOrdersByPaymentStatus();

    /**
     * Find top customers by order count
     */
    @Query("SELECT o.user, COUNT(o) FROM Order o GROUP BY o.user ORDER BY COUNT(o) DESC")
    Page<Object[]> findTopCustomersByOrderCount(Pageable pageable);

    /**
     * Find top customers by order value
     */
    @Query("SELECT o.user, SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED' GROUP BY o.user ORDER BY SUM(o.totalAmount) DESC")
    Page<Object[]> findTopCustomersByOrderValue(Pageable pageable);

    /**
     * Find average order value
     */
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal findAverageOrderValue();

    /**
     * Find orders with specific payment method
     */
    List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod);

    /**
     * Search orders by customer details
     */
    @Query("SELECT o FROM Order o WHERE " +
           "LOWER(o.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(o.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(o.user.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Order> searchOrders(@Param("search") String search, Pageable pageable);
}