package com.enterprise.ecommerce.repository;

import com.enterprise.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity providing data access methods.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find active categories
     */
    List<Category> findByActiveTrue();

    /**
     * Find root categories (no parent)
     */
    List<Category> findByParentCategoryIsNullAndActiveTrueOrderBySortOrder();

    /**
     * Find subcategories of a parent category
     */
    List<Category> findByParentCategoryAndActiveTrueOrderBySortOrder(Category parentCategory);

    /**
     * Find categories by name containing (case insensitive)
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.active = true")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find category hierarchy (parent and all descendants)
     */
    @Query("SELECT c FROM Category c WHERE c = :category OR c.parentCategory = :category")
    List<Category> findCategoryHierarchy(@Param("category") Category category);

    /**
     * Count categories by parent
     */
    @Query("SELECT c.parentCategory, COUNT(c) FROM Category c WHERE c.active = true GROUP BY c.parentCategory")
    List<Object[]> countCategoriesByParent();

    /**
     * Find all active categories ordered by sort order
     */
    List<Category> findByActiveTrueOrderBySortOrder();
}