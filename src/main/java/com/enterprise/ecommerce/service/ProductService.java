package com.enterprise.ecommerce.service;

import com.enterprise.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service interface for product management operations.
 */
public interface ProductService {
    
    ProductDTO createProduct(ProductDTO.CreateProductRequest request);
    
    ProductDTO getProductById(Long id);
    
    ProductDTO getProductBySku(String sku);
    
    ProductDTO updateProduct(Long id, ProductDTO.UpdateProductRequest request);
    
    void deleteProduct(Long id);
    
    Page<ProductDTO> getAllProducts(Pageable pageable);
    
    Page<ProductDTO> searchProducts(String search, Long categoryId, BigDecimal minPrice, 
                                   BigDecimal maxPrice, String brand, boolean featuredOnly, Pageable pageable);
    
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    
    Page<ProductDTO> getFeaturedProducts(Pageable pageable);
    
    Page<ProductDTO> getTopRatedProducts(Pageable pageable);
    
    Page<ProductDTO> getBestSellingProducts(Pageable pageable);
    
    Page<ProductDTO> getRecentProducts(Pageable pageable);
    
    void updateStock(Long id, Integer quantity);
    
    void toggleProductStatus(Long id, boolean active);
    
    void toggleFeaturedStatus(Long id, boolean featured);
}