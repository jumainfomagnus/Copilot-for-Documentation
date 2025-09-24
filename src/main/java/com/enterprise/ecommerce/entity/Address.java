package com.enterprise.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Address entity representing user addresses for shipping and billing.
 */
@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    @Column(nullable = false)
    @NotBlank(message = "Street address is required")
    @Size(max = 255, message = "Street address must not exceed 255 characters")
    private String streetAddress;

    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    private String addressLine2;

    @Column(nullable = false)
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Column(nullable = false)
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Column(nullable = false)
    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Column(nullable = false)
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Column(nullable = false)
    private Boolean isDefault = false;

    @Column(nullable = false)
    private Boolean active = true;

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    @OneToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY)
    private List<Order> shippingOrders;

    @OneToMany(mappedBy = "billingAddress", fetch = FetchType.LAZY)
    private List<Order> billingOrders;

    /**
     * Address type enumeration
     */
    public enum AddressType {
        SHIPPING, BILLING, BOTH
    }

    /**
     * Get full address as a single string
     */
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(streetAddress);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            fullAddress.append(", ").append(addressLine2);
        }
        
        fullAddress.append(", ").append(city)
                   .append(", ").append(state)
                   .append(" ").append(postalCode)
                   .append(", ").append(country);
        
        return fullAddress.toString();
    }

    /**
     * Get recipient name
     */
    public String getRecipientName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return user != null ? user.getFirstName() + " " + user.getLastName() : "";
    }
}