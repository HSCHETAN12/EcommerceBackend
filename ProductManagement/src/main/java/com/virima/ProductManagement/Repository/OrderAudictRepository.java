package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.OrderAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAudictRepository extends JpaRepository<OrderAudit,Integer> {
}
