package com.virima.ProductManagement.Entity;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class Transaction extends Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private double amount;
    private String status;
    private LocalDateTime timestamp;

    @ManyToOne
    private Orders order;
}
