package com.virima.ProductManagement.Service;

import com.virima.ProductManagement.Entity.Product;
import com.virima.ProductManagement.Entity.PromoCode;
import com.virima.ProductManagement.Entity.PromoCodeStatus;
import com.virima.ProductManagement.Entity.PromoCodeType;
import com.virima.ProductManagement.dto.WalletTopUpRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface AdminService {

     ResponseEntity<Object> save( com.virima.ProductManagement.Entity.Admin admin, BindingResult bindingResult);
     ResponseEntity<Object> verify(com.virima.ProductManagement.Entity.Admin admin, HttpSession session);
     ResponseEntity<Object> saveProduct1(String name, String description, double price, int stock, String category, MultipartFile image, HttpSession session);
     ResponseEntity<Object> fetchProducts(HttpSession session);
     ResponseEntity<Object> fetchUser(HttpSession session);
     ResponseEntity<Object> fetchByCategory(String category, HttpSession session);
     ResponseEntity<Object> updateProduct(int id, Product product, HttpSession session);
     ResponseEntity<Object> logOut(HttpSession session);
     ResponseEntity<Object> fetchByName(String name, HttpSession session);
     ResponseEntity<Object> WalletTopUp(WalletTopUpRequest walletTopUpRequest, HttpSession session);
     ResponseEntity<Object> createPromoCode(String code, Double discountValue, PromoCodeType type, LocalDateTime startDate, LocalDateTime endDate, PromoCodeStatus status, String productName, HttpSession session);
     ResponseEntity<Object> UpdatePromoCode(int id, PromoCode promoCode, HttpSession session);
     ResponseEntity<Object> fetchWalletAudict(int user, HttpSession session);

}
