package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.Admin;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findByName(@Size(min = 3,max = 10,message = "Enter minimum 3 charecters") String name);
}
