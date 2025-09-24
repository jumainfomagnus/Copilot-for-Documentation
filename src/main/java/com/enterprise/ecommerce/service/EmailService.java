package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.entity.User;

/**
 * Service interface for email operations.
 */
public interface EmailService {
    
    /**
     * Send email verification message
     */
    void sendVerificationEmail(User user);
    
    /**
     * Send password reset email
     */
    void sendPasswordResetEmail(User user, String resetToken);
    
    /**
     * Send order confirmation email
     */
    void sendOrderConfirmationEmail(User user, String orderNumber);
    
    /**
     * Send order shipped email
     */
    void sendOrderShippedEmail(User user, String orderNumber, String trackingNumber);
}