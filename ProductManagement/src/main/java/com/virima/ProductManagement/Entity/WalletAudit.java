package com.virima.ProductManagement.Entity;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@NoArgsConstructor
public class WalletAudit extends Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int userId;  // User ID who made the transaction
    private String transactionType; // DEBIT or CREDIT
    private double amount;  // Amount added or deducted
    private double balanceAfterTransaction;  // Balance after the transaction
    private LocalDateTime transactiondate;
}
