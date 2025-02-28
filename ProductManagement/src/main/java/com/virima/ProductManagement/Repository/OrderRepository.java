package com.virima.ProductManagement.Repository;


import com.virima.ProductManagement.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
}
