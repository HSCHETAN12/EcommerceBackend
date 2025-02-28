package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.User;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(@Email(message = "It should me proper Email format") @NotEmpty(message = "Email is requried") String email);

    boolean existsByMobile(@DecimalMin(value = "6000000000",message = "It should be proper numbere format") @DecimalMax(value = "9999999999",message = "It should be proper numbere format") Long mobile);

    boolean existsByUsername(@Size(min = 5,max = 15,message = "It should be between 5 and 15 charecters") String username);


    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.verified = true AND u.isDeleted = false")
    List<User> findVerifiedAndNotDeleted();
}
