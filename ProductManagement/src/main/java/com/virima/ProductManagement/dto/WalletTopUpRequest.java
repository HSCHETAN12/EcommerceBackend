package com.virima.ProductManagement.dto;

import lombok.Data;

@Data
public class WalletTopUpRequest {

    private int userId;
    private double amount;
}
