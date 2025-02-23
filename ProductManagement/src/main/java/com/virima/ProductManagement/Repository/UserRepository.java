package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.dto.User;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(@Email(message = "It should me proper Email format") @NotEmpty(message = "Email is requried") String email);

    boolean existsByMobile(@DecimalMin(value = "6000000000",message = "It should be proper numbere format") @DecimalMax(value = "9999999999",message = "It should be proper numbere format") Long mobile);

    boolean existsByUsername(@Size(min = 5,max = 15,message = "It should be between 5 and 15 charecters") String username);
}
