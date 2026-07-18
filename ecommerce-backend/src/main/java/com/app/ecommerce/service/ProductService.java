package com.app.ecommerce.service;

import com.app.ecommerce.dto.products.ProductRequest;
import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService{

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product  product = mapRequestToProduct(productRequest);
        Product savedProduct = productRepository.save(product);
        return mapProductToResponse(savedProduct);
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
}
