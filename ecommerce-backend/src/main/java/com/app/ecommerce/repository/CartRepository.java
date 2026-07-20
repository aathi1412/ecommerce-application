package com.app.ecommerce.repository;

import com.app.ecommerce.models.CartItem;
import com.app.ecommerce.models.Product;
import com.app.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
