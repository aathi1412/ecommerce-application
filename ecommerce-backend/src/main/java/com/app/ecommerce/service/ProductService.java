package com.app.ecommerce.service;

import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.products.ProductRequest;
import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.exceptions.ProductNotFoundException;
import com.app.ecommerce.exceptions.UserNotFoundException;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService{

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream()
                .map(this::mapProductToResponse)
                .toList();
    }

    public List<ProductResponse> getActiveProducts(){
        return productRepository.findByActiveTrue().stream()
                .map(this::mapProductToResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id){
        return productRepository.findById(id)
                .map(this::mapProductToResponse)
                .orElseThrow(() -> {
                    log.error("Product not found with id {}", id);
                    return new ProductNotFoundException("Product Not Found");
                });

    }

    public ProductResponse createProduct(ProductRequest request){
        log.info("Creating product: name='{}', category='{}'",
                request.name(), request.category());

        Product  product = mapRequestToProduct(request);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id={}", savedProduct.getId());

        return mapProductToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, request);
                    log.info("Product updated successfully with id={}", existingProduct.getId());
                    return mapProductToResponse(existingProduct);
                })
                .orElseThrow(() -> {
                    log.warn("Product not found with id={}", id);
                    return new UserNotFoundException("Product id: " + id + "not found");
                });
    }

    public ApiResponse deleteProductById(Long id) {
        productRepository.deleteById(id);

        return ApiResponse.builder()
                .timeStamp(Instant.now())
                .status(HttpStatus.OK.value())
                .message("Product Deleted Successfully")
                .build();
    }

    public List<ProductResponse> searchProducts(String keyword){
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapProductToResponse)
                .toList();
    }
    private ProductResponse mapProductToResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .build();
    }

    private Product mapRequestToProduct(ProductRequest request){
        return Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(request.category())
                .imageUrl(request.imageUrl())
                .build();
    }

    private void updateProductFromRequest(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
    }


}
