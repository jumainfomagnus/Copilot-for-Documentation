package com.enterprise.ecommerce.dto;

import com.enterprise.ecommerce.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for Address entity.
 */
@Data
public class AddressDTO {
    private Long id;
    private Long userId;
    
    @NotNull(message = "Address type is required")
    private Address.AddressType type;
    
    @NotBlank(message = "Street address is required")
    @Size(max = 255, message = "Street address must not exceed 255 characters")
    private String streetAddress;
    
    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;
    
    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;
    
    private Boolean isDefault;
    private Boolean active;
    
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;
    
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String company;

    /**
     * Request DTO for address creation
     */
    @Data
    public static class CreateAddressRequest {
        @NotNull(message = "Address type is required")
        private Address.AddressType type;
        
        @NotBlank(message = "Street address is required")
        @Size(max = 255, message = "Street address must not exceed 255 characters")
        private String streetAddress;
        
        @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
        private String addressLine2;
        
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        private String city;
        
        @NotBlank(message = "State is required")
        @Size(max = 100, message = "State must not exceed 100 characters")
        private String state;
        
        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        private String postalCode;
        
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        private String country;
        
        private Boolean isDefault = false;
        
        @Size(max = 100, message = "First name must not exceed 100 characters")
        private String firstName;
        
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        private String lastName;
        
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        private String phoneNumber;
        
        @Size(max = 100, message = "Company name must not exceed 100 characters")
        private String company;
    }
}