package com.virima.ProductManagement.dto;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Admin extends Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 3,max = 10,message = "Enter minimum 3 charecters")
    private String name;
    @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",message = "It should contain atleast 8 charecter, one uppercase, one lowercase, one number and one speacial charecter")
    private String password;

}
