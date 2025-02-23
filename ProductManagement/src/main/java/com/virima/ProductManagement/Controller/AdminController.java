package com.virima.ProductManagement.Controller;



import com.virima.ProductManagement.Service.AdminService;
import com.virima.ProductManagement.dto.Admin;
import com.virima.ProductManagement.dto.Category;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    AdminService service;


    @PostMapping("/signup")
    public ResponseEntity<Object> save(@Valid @RequestBody Admin admin, BindingResult bindingResult)
    {
        return service.save(admin,bindingResult);
    }

    @PostMapping("/verifiys")
    public ResponseEntity<Object> verify(@RequestBody Admin admin, HttpSession session)
    {
        return service.verify(admin,session);
    }

    @PostMapping("/categorys")
    public ResponseEntity<Object> savecategory(@RequestBody Category category,HttpSession session)
    {
        System.out.println(session.getAttribute(""));
        return service.savecategory(category,session);
    }

    @PostMapping("/products")
    public ResponseEntity<Object> saveProduct(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("price") double price,
                                              @RequestParam("stock") int stock,
                                              @RequestParam("status") String status,
                                              @RequestParam("category") String category,
                                              @RequestParam("image") MultipartFile image,
                                              HttpSession session) {
        return service.saveProduct1(name,description,price,stock,status,category,image,session);
    }

    @GetMapping("/products")
    public ResponseEntity<Object> fetch(HttpSession session)
    {
        return service.fetch(session);
    }

}
