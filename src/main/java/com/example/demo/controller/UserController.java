package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private WalletService walletService;

    // Request body for creating a user
    public static class CreateUserRequest {
        public String name;
        public String email;
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest req) {
        return walletService.createUserWithWallet(req.name, req.email);
    }
}