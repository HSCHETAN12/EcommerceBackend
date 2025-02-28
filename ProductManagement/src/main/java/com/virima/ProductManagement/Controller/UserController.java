package com.virima.ProductManagement.Controller;


import com.virima.ProductManagement.Entity.User;
import com.virima.ProductManagement.ProductException;
import com.virima.ProductManagement.ServiceImp.UserServiceImp;
import com.virima.ProductManagement.dto.CartRequest;
import com.virima.ProductManagement.dto.UserLogins;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/Users")
public class UserController {

    @Autowired
    UserServiceImp userServiceImp;

    Map<String, Object> map = new HashMap<>();

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody User user, BindingResult bindingResult, HttpSession session) {
        return userServiceImp.save(user, bindingResult, session);
    }

    @PostMapping("/vreifys/{id}")
    public ResponseEntity<Object> signup(@RequestParam int otp, @PathVariable int id) {
        return userServiceImp.verifys(otp, id);
    }

    @PostMapping("/logins")
    public ResponseEntity<Object> login(@RequestBody UserLogins userLogins, HttpSession session) {
        return userServiceImp.login(userLogins, session);
    }

    @GetMapping("/logouts")
    public ResponseEntity<Object> logOut(HttpSession session) {
        return userServiceImp.logOut(session);
    }

    //    @PostMapping("/cart/add")
//    public ResponseEntity<Object> addCart(@RequestBody CartRequest cart, HttpSession session)
//    {
//        User user=(User) session.getAttribute("user");
//        if(user==null){
//            map.put("message","User Not Login");
//            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//        }
//        try {
//            return userService.addCart(user.getId(), cart.getProductId(), cart.getQuantity());
//        } catch (ProductException e) {
//            map.put("message","Product Not Found");
//            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
//        }
//    }
    @PostMapping("/cart/add")
    public ResponseEntity<Object> addCart(@RequestBody List<CartRequest> cart, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            map.put("message", "User Not Login");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        try {
            return userServiceImp.addProductsToCart(user.getId(), cart);
        } catch (ProductException e) {
            map.put("message", "Product Not Found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/promocode/{code}")
    public ResponseEntity<Object> applyPromocode(@PathVariable String code,HttpSession session)
    {
        return userServiceImp.applyPromocode(code,session);
    }

    @PostMapping("/checkouts")
    public ResponseEntity<Object> checkouts(HttpSession session){
        try {
            return userServiceImp.checkouts(session);
        } catch (ProductException e) {
            map.put("message","No order Found");
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/products")
    public ResponseEntity<Object> fetchProducts(HttpSession session)
    {
        return userServiceImp.fetchProducts(session);
    }

}
