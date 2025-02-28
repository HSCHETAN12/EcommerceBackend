package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByUserIdAndStatus(int userId, String status);

    Cart findByUserId(int id);
}
