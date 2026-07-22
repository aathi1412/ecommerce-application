package com.app.ecommerce.repository;

import com.app.ecommerce.models.Cart;
import com.app.ecommerce.models.CartItem;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUser(User user);

    void deleteByUser(User user);
}
