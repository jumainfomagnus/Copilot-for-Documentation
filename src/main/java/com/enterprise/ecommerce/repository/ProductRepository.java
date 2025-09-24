package com.enterprise.ecommerce.repository;

import com.enterprise.ecommerce.entity.Product;
import com.enterprise.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity providing data access methods.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Check if SKU exists
     */
    boolean existsBySku(String sku);

    /**
     * Find active products
     */
    List<Product> findByActiveTrue();

    /**
     * Find products by category
     */
    List<Product> findByCategoryAndActiveTrue(Category category);

    /**
     * Find products by category with pagination
     */
    Page<Product> findByCategoryAndActiveTrue(Category category, Pageable pageable);

    /**
     * Find featured products
     */
    List<Product> findByFeaturedTrueAndActiveTrue();

    /**
     * Find products by status
     */
    List<Product> findByStatus(Product.ProductStatus status);

    /**
     * Find products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minimumStockLevel")
    List<Product> findLowStockProducts();

    /**
     * Find out of stock products
     */
    List<Product> findByStockQuantityLessThanEqual(Integer quantity);

    /**
     * Find products by price range
     */
    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find products by brand
     */
    List<Product> findByBrandIgnoreCaseAndActiveTrue(String brand);

    /**
     * Search products by name
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Full text search across multiple fields
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND (" +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);

    /**
     * Find top rated products
     */
    @Query("SELECT p FROM Product p WHERE p.averageRating IS NOT NULL AND p.active = true ORDER BY p.averageRating DESC")
    Page<Product> findTopRatedProducts(Pageable pageable);

    /**
     * Find best selling products
     */
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.salesCount DESC")
    Page<Product> findBestSellingProducts(Pageable pageable);

    /**
     * Find most viewed products
     */
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.viewCount DESC")
    Page<Product> findMostViewedProducts(Pageable pageable);

    /**
     * Find recently added products
     */
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.createdDate DESC")
    Page<Product> findRecentProducts(Pageable pageable);

    /**
     * Update product view count
     */
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId")
    void incrementViewCount(@Param("productId") Long productId);

    /**
     * Update product sales count
     */
    @Modifying
    @Query("UPDATE Product p SET p.salesCount = p.salesCount + :quantity WHERE p.id = :productId")
    void incrementSalesCount(@Param("productId") Long productId, @Param("quantity") Long quantity);

    /**
     * Update product stock quantity
     */
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :productId AND p.stockQuantity >= :quantity")
    int decrementStockQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * Update product rating
     */
    @Modifying
    @Query("UPDATE Product p SET p.averageRating = :rating, p.ratingCount = :count WHERE p.id = :productId")
    void updateProductRating(@Param("productId") Long productId, @Param("rating") BigDecimal rating, @Param("count") Integer count);

    /**
     * Find products created within date range
     */
    @Query("SELECT p FROM Product p WHERE p.createdDate BETWEEN :startDate AND :endDate")
    List<Product> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Count products by category
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.active = true GROUP BY p.category")
    List<Object[]> countProductsByCategory();

    /**
     * Count products by status
     */
    @Query("SELECT p.status, COUNT(p) FROM Product p GROUP BY p.status")
    List<Object[]> countProductsByStatus();

    /**
     * Find products by multiple categories
     */
    @Query("SELECT p FROM Product p WHERE p.category IN :categories AND p.active = true")
    List<Product> findByCategoriesIn(@Param("categories") List<Category> categories);
}