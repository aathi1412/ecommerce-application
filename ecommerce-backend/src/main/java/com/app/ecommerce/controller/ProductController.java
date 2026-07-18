package com.app.ecommerce.controller;

import com.app.ecommerce.dto.products.ProductRequest;
import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(ProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
