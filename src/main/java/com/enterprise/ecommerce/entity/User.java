package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents a user in the e-commerce platform with authentication and authorization capabilities.
 * <p>
 * This entity integrates with Spring Security by implementing {@link org.springframework.security.core.userdetails.UserDetails}.
 * It contains user profile information, security status, roles, and relationships to addresses, orders, and shopping cart.
 * </p>
 * <h2>Fields</h2>
 * <ul>
 *   <li><b>username</b>: Unique username for login and identification.</li>
 *   <li><b>email</b>: Unique, validated email address.</li>
 *   <li><b>password</b>: Hashed password for authentication.</li>
 *   <li><b>firstName</b>, <b>lastName</b>: User's personal names.</li>
 *   <li><b>phoneNumber</b>: Optional contact number.</li>
 *   <li><b>enabled</b>: Indicates if the account is enabled.</li>
 *   <li><b>accountNonExpired</b>, <b>accountNonLocked</b>, <b>credentialsNonExpired</b>: Security flags for account status.</li>
 *   <li><b>emailVerified</b>: Indicates if the email is verified.</li>
 *   <li><b>lastLoginDate</b>: Timestamp of last successful login.</li>
 *   <li><b>failedLoginAttempts</b>: Number of consecutive failed login attempts.</li>
 *   <li><b>lockoutTime</b>: Timestamp when account was locked out.</li>
 *   <li><b>status</b>: Current user status (ACTIVE, INACTIVE, etc.).</li>
 *   <li><b>roles</b>: Set of roles assigned to the user.</li>
 *   <li><b>addresses</b>: List of addresses associated with the user.</li>
 *   <li><b>orders</b>: List of orders placed by the user.</li>
 *   <li><b>shoppingCart</b>: User's shopping cart.</li>
 * </ul>
 * <h2>Usage Example</h2>
 * <pre>
 * User user = new User();
 * user.setUsername("john_doe");
 * user.setEmail("john@example.com");
 * user.setPassword(passwordEncoder.encode("securePassword123"));
 * user.setFirstName("John");
 * user.setLastName("Doe");
 * user.setRoles(Set.of(User.Role.USER));
 * user.setStatus(User.UserStatus.ACTIVE);
 * </pre>
 * <h2>Best Practices</h2>
 * <ul>
 *   <li>Always store passwords in a hashed format using a secure encoder.</li>
 *   <li>Assign roles and statuses carefully to control access and permissions.</li>
 *   <li>Use validation annotations to ensure data integrity.</li>
 *   <li>Leverage the security flags to implement account lockout and expiration policies.</li>
 *   <li>Integrate with Spring Security for authentication and authorization.</li>
 * </ul>
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {


    /**
     * Unique username for login and identification.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * Unique, validated email address of the user.
     */
    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * Hashed password for authentication.
     */
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /**
     * User's first name.
     */
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    /**
     * User's last name.
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    /**
     * Optional phone number for contact.
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    /**
     * Indicates if the account is enabled.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Indicates if the account is not expired.
     */
    @Column(nullable = false)
    private Boolean accountNonExpired = true;

    /**
     * Indicates if the account is not locked.
     */
    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    /**
     * Indicates if the credentials are not expired.
     */
    @Column(nullable = false)
    private Boolean credentialsNonExpired = true;

    /**
     * Indicates if the email is verified.
     */
    @Column(nullable = false)
    private Boolean emailVerified = false;

    /**
     * Timestamp of the last successful login.
     */
    private LocalDateTime lastLoginDate;

    /**
     * Number of consecutive failed login attempts.
     */
    private Integer failedLoginAttempts = 0;

    /**
     * Timestamp when the account was locked out.
     */
    private LocalDateTime lockoutTime;

    /**
     * Current user status (ACTIVE, INACTIVE, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Set of roles assigned to the user.
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    /**
     * List of addresses associated with the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    /**
     * List of orders placed by the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    /**
     * User's shopping cart.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;

    /**
     * Returns the authorities granted to the user based on their roles.
     * <p>
     * Each role is mapped to a {@link org.springframework.security.core.authority.SimpleGrantedAuthority}
     * with the prefix "ROLE_".
     * </p>
     * @return collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    /**
     * Indicates whether the user's account has expired.
     * @return true if account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * <p>
     * Returns false if the account is locked and the lockout time is in the future.
     * </p>
     * @return true if account is non-locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked && (lockoutTime == null || lockoutTime.isBefore(LocalDateTime.now()));
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * @return true if credentials are non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Indicates whether the user is enabled.
     * <p>
     * User is enabled only if the enabled flag is true and status is ACTIVE.
     * </p>
     * @return true if user is enabled
     */
    @Override
    public boolean isEnabled() {
        return enabled && status == UserStatus.ACTIVE;
    }

    /**
     * Enumeration of possible user statuses.
     * <ul>
     *   <li>ACTIVE: User is active and can log in.</li>
     *   <li>INACTIVE: User is inactive and cannot log in.</li>
     *   <li>SUSPENDED: User is suspended due to policy violations.</li>
     *   <li>PENDING_VERIFICATION: User must verify email before activation.</li>
     * </ul>
     */
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION
    }

    /**
     * Enumeration of possible user roles.
     * <ul>
     *   <li>USER: Standard user role.</li>
     *   <li>ADMIN: Administrator with full access.</li>
     *   <li>MANAGER: Manager role for business operations.</li>
     *   <li>CUSTOMER_SERVICE: Customer service representative.</li>
     *   <li>INVENTORY_MANAGER: Inventory management role.</li>
     * </ul>
     */
    public enum Role {
        USER, ADMIN, MANAGER, CUSTOMER_SERVICE, INVENTORY_MANAGER
    }
}