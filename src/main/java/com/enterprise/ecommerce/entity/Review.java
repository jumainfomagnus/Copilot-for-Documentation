package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Review entity representing customer reviews for products.
 */
@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Column(length = 2000)
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;

    @Column(nullable = false)
    private Boolean approved = false;

    @Column(nullable = false)
    private Boolean verified = false; // Verified purchase

    private Integer helpfulCount = 0;

    private Integer unhelpfulCount = 0;
}