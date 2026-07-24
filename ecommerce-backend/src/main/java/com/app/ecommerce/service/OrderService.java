package com.app.ecommerce.service;

import com.app.ecommerce.dto.orders.OrderItemDTO;
import com.app.ecommerce.dto.orders.OrderResponse;
import com.app.ecommerce.enums.OrderStatus;
import com.app.ecommerce.models.*;
import com.app.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;

    @Transactional
    public OrderResponse createOrder(Long userId) {
        User user = userService.getUser(userId);
        Cart cart = cartService.getCartItems(userId);

        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalPrice)
                .orderStatus(OrderStatus.CONFIRMED)
                .build();

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(items -> OrderItem.builder()
                        .product(items.getProduct())
                        .order(order)
                        .quantity(items.getQuantity())
                        .price(items.getPrice())
                        .build())
                .toList();

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return  OrderResponse.builder()
                    .id(order.getId())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getOrderStatus())
                    .OrderItems(order.getOrderItems().stream()
                            .map(orderItem -> OrderItemDTO.builder()
                                        .id(orderItem.getId())
                                        .productId(orderItem.getProduct().getId())
                                        .quantity(orderItem.getQuantity())
                                        .price(orderItem.getPrice())
                                        .subTotal(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                                        .build()
                            )
                            .toList()
                    )
                    .createdAt(order.getCreatedAt())
                    .build();
    }


}
