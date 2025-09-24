package com.enterprise.ecommerce.controller;

import com.enterprise.ecommerce.dto.ProductDTO;
import com.enterprise.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for product management operations.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Add a new product to the catalog")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO.CreateProductRequest request) {
        log.info("Received request to create product with SKU: {}", request.getSku());
        ProductDTO createdProduct = productService.createProduct(request);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve product details by product ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get product by SKU", description = "Retrieve product details by SKU")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        ProductDTO product = productService.getProductBySku(sku);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Update product", description = "Update product information")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody ProductDTO.UpdateProductRequest request) {
        log.info("Received request to update product with ID: {}", id);
        ProductDTO updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete product", description = "Remove a product from the catalog")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        log.info("Received request to delete product with ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all products", description = "Retrieve all products with pagination")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Search products", description = "Search products by various criteria")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @Parameter(description = "Search query") @RequestParam(required = false) String search,
            @Parameter(description = "Category ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Minimum price") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Brand") @RequestParam(required = false) String brand,
            @Parameter(description = "Featured only") @RequestParam(defaultValue = "false") boolean featuredOnly,
            Pageable pageable) {
        Page<ProductDTO> products = productService.searchProducts(search, categoryId, minPrice, maxPrice, brand, featuredOnly, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by category", description = "Retrieve products in a specific category")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get featured products", description = "Retrieve featured products")
    @ApiResponse(responseCode = "200", description = "Featured products retrieved successfully")
    @GetMapping("/featured")
    public ResponseEntity<Page<ProductDTO>> getFeaturedProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getFeaturedProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get top rated products", description = "Retrieve top rated products")
    @ApiResponse(responseCode = "200", description = "Top rated products retrieved successfully")
    @GetMapping("/top-rated")
    public ResponseEntity<Page<ProductDTO>> getTopRatedProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getTopRatedProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get best selling products", description = "Retrieve best selling products")
    @ApiResponse(responseCode = "200", description = "Best selling products retrieved successfully")
    @GetMapping("/best-selling")
    public ResponseEntity<Page<ProductDTO>> getBestSellingProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getBestSellingProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get recent products", description = "Retrieve recently added products")
    @ApiResponse(responseCode = "200", description = "Recent products retrieved successfully")
    @GetMapping("/recent")
    public ResponseEntity<Page<ProductDTO>> getRecentProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getRecentProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update stock quantity", description = "Update product stock quantity")
    @ApiResponse(responseCode = "200", description = "Stock updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> updateStock(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "New stock quantity") @RequestParam Integer quantity) {
        log.info("Received request to update stock for product ID: {} to {}", id, quantity);
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Toggle product active status", description = "Enable or disable a product")
    @ApiResponse(responseCode = "200", description = "Product status updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> toggleProductStatus(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Active status") @RequestParam boolean active) {
        log.info("Received request to toggle status for product ID: {} to {}", id, active);
        productService.toggleProductStatus(id, active);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Toggle featured status", description = "Mark or unmark product as featured")
    @ApiResponse(responseCode = "200", description = "Featured status updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> toggleFeaturedStatus(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Featured status") @RequestParam boolean featured) {
        log.info("Received request to toggle featured status for product ID: {} to {}", id, featured);
        productService.toggleFeaturedStatus(id, featured);
        return ResponseEntity.ok().build();
    }
}