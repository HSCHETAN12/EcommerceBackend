package com.virima.ProductManagement.Controller;


import com.virima.ProductManagement.Service.UserService;
import com.virima.ProductManagement.dto.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/Users")
public class UserController {

    @Autowired
    UserService service;

@PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody User user, BindingResult bindingResult, HttpSession session)
{
        return service.save(user,bindingResult,session);
}
}
