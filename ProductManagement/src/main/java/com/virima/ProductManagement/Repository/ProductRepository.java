package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByIsDeletedFalseAndStockGreaterThan(int stock);

    List<Product> findByCategory(String category);

    List<Product> findByName(String name);

}
