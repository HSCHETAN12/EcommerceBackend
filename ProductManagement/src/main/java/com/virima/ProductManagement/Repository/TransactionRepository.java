package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
}
