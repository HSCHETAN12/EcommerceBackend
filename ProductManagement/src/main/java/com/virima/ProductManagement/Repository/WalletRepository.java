package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.User;
import com.virima.ProductManagement.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Wallet findByUserId(int userId);
}
