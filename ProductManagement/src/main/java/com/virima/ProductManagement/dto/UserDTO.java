package com.virima.ProductManagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String userName;
    private String username;
    private String email;
    private Double walletBalance;
    private Long number;
    private String gender;

    // Constructor
    public UserDTO(int id, String userName, String username, String email, Double walletBalance, Long number, String gender) {
        this.id = id;
        this.userName = userName;
        this.username = username;
        this.email = email;
        this.walletBalance = walletBalance;
        this.number = number;
        this.gender = gender;
    }

}
