package com.app.ecommerce.controller;

import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.products.ProductRequest;
import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity
                .ok(productService.getAllProducts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts(){
        return ResponseEntity
                .ok(productService.getActiveProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity
                .ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request){
        return ResponseEntity
                .ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        ApiResponse response = productService.deleteProductById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

}
