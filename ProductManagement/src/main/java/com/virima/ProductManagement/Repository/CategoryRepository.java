package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.dto.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Category findByName(String name);
}
