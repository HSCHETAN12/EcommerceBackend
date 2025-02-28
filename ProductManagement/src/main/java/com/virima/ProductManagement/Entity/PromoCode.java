package com.virima.ProductManagement.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private Double discountValue;
    private PromoCodeType type;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PromoCodeStatus status;

    private String productName;



    public PromoCode(String code, Double discountValue, PromoCodeType type,
                     LocalDateTime startDate, LocalDateTime endDate,
                     PromoCodeStatus status, String productName) {
        this.code = code;
        this.discountValue = discountValue;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.productName = productName;
    }


    public boolean isValid() {
        return LocalDateTime.now().isAfter(startDate) && LocalDateTime.now().isBefore(endDate) && status == PromoCodeStatus.ACTIVE;
    }
}
