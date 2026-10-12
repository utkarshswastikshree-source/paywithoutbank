package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Earth! I am building a backend.";
    }

    @GetMapping("/hello/{name}")
    public String greetUser(@PathVariable String name) {
        return "Hello " + name + "! Welcome to your first REST API.";
    }
}