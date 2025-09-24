package com.enterprise.ecommerce.repository;

import com.enterprise.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity providing data access methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username or email
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by status
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * Find users by enabled status
     */
    List<User> findByEnabled(Boolean enabled);

    /**
     * Find users with specific roles
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r IN :roles")
    List<User> findByRolesIn(@Param("roles") List<User.Role> roles);

    /**
     * Find users created within date range
     */
    @Query("SELECT u FROM User u WHERE u.createdDate BETWEEN :startDate AND :endDate")
    List<User> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Find users who haven't logged in recently
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginDate < :date OR u.lastLoginDate IS NULL")
    List<User> findInactiveUsersSince(@Param("date") LocalDateTime date);

    /**
     * Find users by email verification status
     */
    List<User> findByEmailVerified(Boolean emailVerified);

    /**
     * Update user last login date
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginDate = :loginDate WHERE u.id = :userId")
    void updateLastLoginDate(@Param("userId") Long userId, @Param("loginDate") LocalDateTime loginDate);

    /**
     * Update failed login attempts
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") Long userId, @Param("attempts") Integer attempts);

    /**
     * Lock user account
     */
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = false, u.lockoutTime = :lockoutTime WHERE u.id = :userId")
    void lockUserAccount(@Param("userId") Long userId, @Param("lockoutTime") LocalDateTime lockoutTime);

    /**
     * Unlock user account
     */
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = true, u.lockoutTime = null, u.failedLoginAttempts = 0 WHERE u.id = :userId")
    void unlockUserAccount(@Param("userId") Long userId);

    /**
     * Find users with pagination and search
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findUsersWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Count users by status
     */
    @Query("SELECT u.status, COUNT(u) FROM User u GROUP BY u.status")
    List<Object[]> countUsersByStatus();
}