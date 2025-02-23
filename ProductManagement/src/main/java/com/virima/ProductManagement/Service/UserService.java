package com.virima.ProductManagement.Service;


import com.virima.ProductManagement.Helper.AES;
import com.virima.ProductManagement.Helper.EmailSender;
import com.virima.ProductManagement.Repository.UserRepository;
import com.virima.ProductManagement.dto.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    EmailSender emailSender;

        public ResponseEntity<Object> save(User user, BindingResult bindingResult, HttpSession session) {


            if(repository.existsByEmail(user.getEmail()))
                bindingResult.rejectValue("email", "error.email", "Email is alreday exist");
            if(repository.existsByMobile(user.getMobile()))
                bindingResult.rejectValue("mobile", "error.mobile", "Number is alreday exist");
            if(repository.existsByUsername(user.getUsername()))
                bindingResult.rejectValue("username", "error.username", "username is alreday taken");

            // Check for validation errors
            if (bindingResult.hasErrors()) {
                // Collect all the error messages
                StringBuilder errors = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> {
                    errors.append(error.getDefaultMessage()).append("\n");
                });
                Map<String,Object> map=new HashMap<>();
                map.put("message",errors);
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }

            // If no validation errors, save the product
            else {
                user.setPassword(AES.encrypt(user.getPassword()));
                int otp=new Random().nextInt(10000,100000);
                user.setOtp(otp);
                System.err.println(otp);
			    emailSender.sendOtp(user.getEmail(),otp , user.getName());
                repository.save(user);
                session.setAttribute("pass", "otp-sent Success");
                Map<String,Object> map=new HashMap<>();
                map.put("message","User added successfully");
               return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }
    }

