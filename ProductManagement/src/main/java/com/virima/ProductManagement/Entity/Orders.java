package com.virima.ProductManagement.Entity;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Orders extends Parent{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private double totalPrice;
    private String orderStatus;

}
