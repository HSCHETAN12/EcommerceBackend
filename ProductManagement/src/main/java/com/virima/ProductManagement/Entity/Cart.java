package com.virima.ProductManagement.Entity;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class Cart extends Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private double totalAmount;

    private String status ;

    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    private List<CartItem> cartItems;

    // Getters and setters omitted for brevity

    public Cart(int userId) {
        this.userId = userId;
        this.totalAmount = 0.0;
        this.status="active";
    }

    public void updateTotalAmount(Product product) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();// Ensure cartItems is never null
            System.out.print("Hi");
        }
        this.totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * product.getPrice())
                .sum();
        System.out.print(totalAmount);
    }
}
