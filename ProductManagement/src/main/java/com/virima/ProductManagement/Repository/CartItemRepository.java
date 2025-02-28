package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    CartItem findByCartIdAndProductId(int cartId, int productId);
}
