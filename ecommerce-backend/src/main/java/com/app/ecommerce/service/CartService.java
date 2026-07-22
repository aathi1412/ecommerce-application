package com.app.ecommerce.service;

import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartItemResponse;
import com.app.ecommerce.dto.cart.CartRequest;
import com.app.ecommerce.dto.cart.CartResponse;
import com.app.ecommerce.exceptions.*;
import com.app.ecommerce.exceptions.cart.CartItemNotFoundException;
import com.app.ecommerce.exceptions.cart.CartNotFoundException;
import com.app.ecommerce.exceptions.cart.InsufficientStockException;
import com.app.ecommerce.exceptions.cart.OutOfStockException;
import com.app.ecommerce.models.Cart;
import com.app.ecommerce.models.CartItem;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.models.User;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.ProductRepository;
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
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private  final UserService userService;


    public List<CartResponse> getCartResponse(Long userId) {
        User user = userService.getUser(userId);
        return cartRepository.findByUser(user).stream()
                .map(this::mapToCartResponse)
                .toList();
    }

    public Cart getCartItems(Long userId) {
        User user = userService.getUser(userId);
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("cart not found"));
    }

    @Transactional
    public ApiResponse addToCart(Long userId, CartRequest request) {

        User user = userService.getUser(userId);
        Product product = getProduct(request.productId());

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> Cart.builder()
                        .user(user)
                        .build());

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + request.quantity();

            validateStockQuantity(product, newQuantity);

            existingItem.setQuantity(newQuantity);

        } else {

            validateStockQuantity(product, request.quantity());

            cart.getCartItems().add(
                    CartItem.builder()
                            .product(product)
                            .cart(cart)
                            .quantity(request.quantity())
                            .price(product.getPrice())
                            .build()
            );
        }

        cartRepository.save(cart);
        log.info("Added new product {} to cart for user {}", product.getId(), userId);

        return buildApiResponse(HttpStatus.CREATED, "Successfully added cart item");
    }

    @Transactional
    public ApiResponse updateCartItemQuantity(Long userId, CartRequest request) {
        Product product = getProduct(request.productId());

        User user = userService.getUser(userId);

        validateStockQuantity(product, request.quantity());

        Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> {
                        log.warn("Cart item not found for user {} and product {}", userId, product.getId());
                        return new CartNotFoundException("Cart item not found");
                    });

        CartItem cartItem = cart.getCartItems().stream()
                        .filter(item ->  item.getProduct().getId().equals(request.productId()))
                        .findFirst()
                        .orElseThrow(() -> new CartNotFoundException("Cart item not found"));

        cartItem.setQuantity(request.quantity());
        cartItem.setPrice(product.getPrice());

        return buildApiResponse(HttpStatus.OK, "Successfully updated cart item");
    }

    public void clearCart(User user) {
        cartRepository.deleteByUser(user);
    }

    @Transactional
    public ApiResponse removeProductFromCart(Long userId, Long productId) {
        User user = userService.getUser(userId);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        boolean removed = cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        if(!removed) {
            throw  new CartItemNotFoundException("Cart item not found");
        }

        return  buildApiResponse(HttpStatus.OK, "Successfully deleted cart item");
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with id {}", productId);
                    return new ProductNotFoundException("Product not found");
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

    public CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .cartItems(cart.getCartItems().stream()
                        .map(this::mapToCartItemResponse)
                        .toList()
                )
                .totalPrice(getTotalPrice(cart))
                .totalQuantity(getTotalCartItems(cart))
                .totalProducts(cart.getCartItems().size())
                .createdAt(cart.getCreatedAt())
                .build();
    }

    public CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productImage(cartItem.getProduct().getImageUrl())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .build();
    }

    public BigDecimal getTotalPrice(Cart  cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getTotalCartItems(Cart  cart) {
        return cart.getCartItems().stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
    }


}
