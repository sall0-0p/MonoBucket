package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.requests.LoginRequest;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (userService.authenticate(user.getId(), loginRequest.getPin())) {
            request.getSession(true).setAttribute("user", user);
            return ResponseEntity.ok("Logged in successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (request != null && request.getSession(false) != null && request.getAttribute("user") != null) {
            request.setAttribute("user", null);
            return ResponseEntity.ok("Logged out");
        } else {
            return ResponseEntity.badRequest().body("Something went wrong.");
        }
    }
}
