package com.virima.ProductManagement.Service;


import com.virima.ProductManagement.Helper.AES;
import com.virima.ProductManagement.Helper.CloudinaryHelper;
import com.virima.ProductManagement.Repository.AdminRepository;
import com.virima.ProductManagement.Repository.CategoryRepository;
import com.virima.ProductManagement.Repository.ProductRepository;
import com.virima.ProductManagement.dto.Admin;
import com.virima.ProductManagement.dto.Category;
import com.virima.ProductManagement.dto.Product;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AdminService {
    @Autowired
    AdminRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CloudinaryHelper cloudinaryHelper;

    Map<String,Object> map=new HashMap<>();

    public ResponseEntity<Object> save(@Valid Admin admin, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Collect all the error messages
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errors.append(error.getDefaultMessage()).append("\n");
            });

            map.put("message",errors);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // If no validation errors, save the product
        else {
            admin.setPassword(AES.encrypt(admin.getPassword()));

            repository.save(admin);
            Map<String,Object> map=new HashMap<>();
            map.put("message","Admin added successfully");
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> verify(Admin admin, HttpSession session) {
        Admin admin1 = repository.findByName(admin.getName());
        if (admin1 != null && AES.encrypt(admin.getPassword()).equals(admin1.getPassword())) {
            session.setAttribute("admin", admin1);  // Store admin in session
            map.put("message", "Login Successful");
            return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
        } else {
            map.put("message", "Invalid login credentials");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> savecategory(Category category, HttpSession session) {

        Admin admin=(Admin) session.getAttribute("admin");
        System.out.println(admin);
        if(admin!=null)
        {
            Category category1=categoryRepository.findByName(category.getName());
            if(category1!=null)
            {
                categoryRepository.save(category);
                map.put("message","category added successfully");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }else {
                map.put("message","category is alreday exists");
                return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
            }

        }else{
            session.setAttribute("fail", "Invaild Admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }





    public ResponseEntity<Object> saveProduct1(String name, String description, double price, int stock, String status, String category, MultipartFile image, HttpSession session) {
        Admin admin1=(Admin)session.getAttribute("admin");
        if(admin1==null)
        {
            session.setAttribute("fail", "Invaild Admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }else{
            Category category1=categoryRepository.findByName(category);
            if(category1!=null)
            {
                Product product=new Product();
                product.setCategoryId(category1);
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                product.setStatus(status);
                product.setDescription(description);
                product.setStock(stock);
                product.setImageUrl(cloudinaryHelper.saveImage(image));
                productRepository.save(product);
                map.put("message","Product added Successfully");
                return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
            }else{
                map.put("message","Category Not Found in the Category table plese add category first");
                return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
            }
        }
    }


    public ResponseEntity<Object> fetch(HttpSession session) {
        Admin admin1=(Admin)session.getAttribute("admin");
        if(admin1==null)
        {
            session.setAttribute("fail", "Invaild admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
        }else{
            List<Product> productList=productRepository.findAll();
            System.out.print(productList.size());
           if(productList.isEmpty())
           {
               map.put("message","No product present in the shope");
           }else {
               map.put("data",productList);
               map.put("message","Product List");
               return new ResponseEntity<>(map,HttpStatus.OK);
           }
        }
        return null;
    }
}

