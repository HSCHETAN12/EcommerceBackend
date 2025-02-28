package com.virima.ProductManagement.Controller;



import com.virima.ProductManagement.Entity.*;
import com.virima.ProductManagement.ServiceImp.AdminServiceServiceImp;
import com.virima.ProductManagement.dto.WalletTopUpRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    AdminServiceServiceImp adminServiceImp;

    @PostMapping("/signup")
    public ResponseEntity<Object> save(@Valid @RequestBody Admin admin, BindingResult bindingResult)
    {
        return adminServiceImp.save(admin,bindingResult);
    }

    @PostMapping("/verifiys")
    public ResponseEntity<Object> verify(@RequestBody Admin admin, HttpSession session)
    {
        return adminServiceImp.verify(admin,session);
    }

    @GetMapping("/logouts")
    public ResponseEntity<Object> logOut(HttpSession session)
    {
        return adminServiceImp.logOut(session);
    }

    @PostMapping("/products")
    public ResponseEntity<Object> saveProduct(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("price") double price,
                                              @RequestParam("stock") int stock,
                                              @RequestParam("category") String category, @RequestParam("image") MultipartFile image,
                                              HttpSession session) {
        return adminServiceImp.saveProduct1(name,description,price,stock,category,image,session);
    }

    @GetMapping("/products")
    public ResponseEntity<Object> fetchProducts(HttpSession session)
    {
        return adminServiceImp.fetchProducts(session);
    }

    @GetMapping("/products/{category}")
    public ResponseEntity<Object> fetchByCategory(@PathVariable String category,HttpSession session)
    {
        return adminServiceImp.fetchByCategory(category,session);
    }

    @GetMapping("/products/name/{name}")
    public ResponseEntity<Object> fetchByName(@PathVariable String name,HttpSession session)
    {
        return adminServiceImp.fetchByName(name,session);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> fetchUser(HttpSession session)
    {
        return adminServiceImp.fetchUser(session);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable int id, @RequestBody Product product,HttpSession session)
    {
        return adminServiceImp.updateProduct(id,product,session);
    }

    @PostMapping("/wallettopup")
    public ResponseEntity<Object> WalletTopUp(@RequestBody WalletTopUpRequest walletTopUpRequest,HttpSession session)
    {
        return adminServiceImp.WalletTopUp(walletTopUpRequest,session);
    }

    @PostMapping("/promocode")
    public ResponseEntity<Object> createPromoCode(
            @RequestParam String code,
            @RequestParam Double discountValue,
            @RequestParam PromoCodeType type,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam PromoCodeStatus status,
            @RequestParam(required = false) String productName,HttpSession session) {

        return adminServiceImp.createPromoCode(code, discountValue, type, startDate, endDate, status, productName,session);

    }

    @PatchMapping("/promocode/{id}")
    public ResponseEntity<Object> UpdatePromoCode(@PathVariable int id,@RequestBody PromoCode promoCode,HttpSession session)
    {
        return adminServiceImp.UpdatePromoCode(id,promoCode,session);
    }

    @GetMapping("/walletaudict/{id}")
    public ResponseEntity<Object> fetchWalletAudict(@PathVariable int id,HttpSession session)
    {
        return adminServiceImp.fetchWalletAudict(id,session);
    }

//    @DeleteMapping("/Users/{id}")
//    public ResponseEntity<Object> deletUser(@PathVariable int id, HttpSession session)
//    {
//        return adminService.daletUser(id,session);
//    }
}
