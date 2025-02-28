package com.virima.ProductManagement.Service;

import com.virima.ProductManagement.ProductException;
import com.virima.ProductManagement.dto.CartRequest;
import com.virima.ProductManagement.dto.UserLogins;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {

     ResponseEntity<Object> save(com.virima.ProductManagement.Entity.User user, BindingResult bindingResult, HttpSession session);
     ResponseEntity<Object> verifys(int otp, int id);
     ResponseEntity<Object> login(UserLogins userLogins, HttpSession session);
     ResponseEntity<Object> logOut(HttpSession session);
     ResponseEntity<Object> addProductsToCart(int userId, List<CartRequest> products) throws ProductException;
     ResponseEntity<Object> applyPromocode(String code, HttpSession session);
     ResponseEntity<Object> checkouts(HttpSession session) throws ProductException;
     ResponseEntity<Object> fetchProducts(HttpSession session);

}
