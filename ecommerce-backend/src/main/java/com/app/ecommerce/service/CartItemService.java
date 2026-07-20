package com.app.ecommerce.service;

import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartItemRequest;
import com.app.ecommerce.dto.cart.CartItemResponse;
import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.dto.user.UserResponse;
import com.app.ecommerce.exceptions.*;
import com.app.ecommerce.exceptions.cart.CartItemNotFoundException;
import com.app.ecommerce.exceptions.cart.InsufficientStockException;
import com.app.ecommerce.exceptions.cart.OutOfStockException;
import com.app.ecommerce.models.CartItem;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.models.User;
import com.app.ecommerce.repository.CartItemRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public List<CartItemResponse> getAllCartItems(Long userId) {
        User user = getUser(userId);
        return cartItemRepository.findByUser(user).stream()
                .map(this::mapToCartItemResponse)
                .toList();
    }

    @Transactional
    public ApiResponse addToCart(Long userId, CartItemRequest request) {
        Product product = getProduct(request.productId());

        User user = getUser(userId);

        cartItemRepository.findByUserAndProduct(user, product).
                ifPresentOrElse(
                cartItem -> {
                            int newQuantity = cartItem.getQuantity() + request.quantity();
                            validateStockQuantity(product, newQuantity);

                            cartItem.setQuantity(newQuantity);
                            cartItem.setPrice(product.getPrice()
                                    .multiply(BigDecimal.valueOf(newQuantity)));

                            log.info("new quantity added to cart item for user {}, product id {}", userId, product.getId());
                        },
                        () -> {
                            validateStockQuantity(product, request.quantity());
                                CartItem cartItem = CartItem.builder()
                                            .user(user)
                                            .product(product)
                                            .quantity(request.quantity())
                                            .price(product.getPrice()
                                                    .multiply(BigDecimal.valueOf(request.quantity())))
                                            .build();
                                log.info("Added new product {} to cart for user {}", product.getId(), userId);
                                cartItemRepository.save(cartItem);
                        });

        return buildApiResponse(HttpStatus.CREATED, "Successfully added cart item");
    }

    @Transactional
    public ApiResponse updateCartItemQuantity(Long userId, CartItemRequest request) {
        Product product = getProduct(request.productId());

        User user = getUser(userId);

        validateStockQuantity(product, request.quantity());

        CartItem cartItem = cartItemRepository
                    .findByUserAndProduct(user, product)
                    .orElseThrow(() -> {
                        log.warn("Cart item not found for user {} and product {}", userId, product.getId());
                        return new CartItemNotFoundException("Cart item not found");
                    });

        cartItem.setQuantity(request.quantity());
        cartItem.setPrice(
                product.getPrice().multiply(BigDecimal.valueOf(request.quantity()))
        );

        return buildApiResponse(HttpStatus.OK, "Successfully updated cart item");
    }

    @Transactional
    public ApiResponse deleteCartItem(Long userId, Long productId) {
        Product product = getProduct(productId);
        User user = getUser(userId);

        cartItemRepository.deleteByUserAndProduct(user, product);
        return  buildApiResponse(HttpStatus.OK, "Successfully deleted cart item");
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with id {}", productId);
                    return new ProductNotFoundException("Product not found");
                });
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for id {} while updating cart", userId);
                    return new UserNotFoundException("User not found");
                });
    }

    public void validateStockQuantity(Product product, Integer quantity) {
        if (product.getStockQuantity() == 0) {
            log.warn("Product with id {} and name {} is out of stock", product.getId(), product.getName());
            throw new OutOfStockException("Product is out of stock.");
        }

        if (product.getStockQuantity() < quantity) {
            log.warn("Product with id {} and name {} has less quantity than requested quantity", product.getId(), product.getName());
            throw new InsufficientStockException(
                    "Only " + product.getStockQuantity() + " items are available."
            );
        }
    }

    public ApiResponse buildApiResponse(HttpStatus status, String message) {
        return ApiResponse.builder()
                .timeStamp(Instant.now())
                .status(status.value())
                .message(message)
                .build();
    }

    public CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .product(ProductResponse.builder()
                        .name(cartItem.getProduct().getName())
                        .description(cartItem.getProduct().getDescription())
                        .category(cartItem.getProduct().getCategory())
                        .stockQuantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .active(cartItem.getProduct().getActive())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .user(UserResponse.builder()
                        .firstName(cartItem.getUser().getFirstName())
                        .lastName(cartItem.getUser().getLastName())
                        .build())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }

}
