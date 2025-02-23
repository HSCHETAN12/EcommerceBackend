package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.dto.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {


}
