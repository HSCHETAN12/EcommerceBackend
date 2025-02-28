package com.virima.ProductManagement.Entity;

import com.virima.ProductManagement.Base.Parent;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class User extends Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 3,max = 10,message = "Enter minimum 3 charecters")
    private String name;
    @Size(min = 5,max = 15,message = "It should be between 5 and 15 charecters")
    private String username;
    @Email(message = "It should me proper Email format")
    @NotEmpty(message = "Email is requried")
    private String email;
    @DecimalMin(value = "6000000000",message = "It should be proper numbere format")
    @DecimalMax(value = "9999999999",message = "It should be proper numbere format")
    private Long mobile;
    @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",message = "It should contain atleast 8 charecter, one uppercase, one lowercase, one number and one speacial charecter")
    private String password;
    @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",message = "It should contain atleast 8 charecter, one uppercase, one lowercase, one number and one speacial charecter")
    @Transient
    private String confirmpassword;
    @NotNull(message = "It is required Field")
    private String gender;
    private int otp;
    private boolean verified;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Wallet wallet; // One-to-One relationship with wallet

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Address> addresses;

}
