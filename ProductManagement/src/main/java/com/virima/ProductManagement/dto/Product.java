package com.virima.ProductManagement.dto;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String category;
    @ManyToOne
    private Category categoryId;
    private String status;

}
