package com.vivax.controller;

import com.vivax.dto.ApiResponse;
import com.vivax.dto.AuthRequest;
import com.vivax.dto.AuthResponse;
import com.vivax.entity.User;
import com.vivax.repository.UserRepository;
import com.vivax.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest) {
        String email = authRequest.getEmail() == null ? "" : authRequest.getEmail().trim();
        String password = authRequest.getPassword() == null ? "" : authRequest.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email and password are required", null));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Incorrect email or password", null));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String jwt = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new ApiResponse(true, "Login successful", new AuthResponse(jwt, user)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> me(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .map(u -> ResponseEntity.ok(new ApiResponse(true, "User fetched", u)))
                .orElseGet(() -> ResponseEntity.status(401).body(new ApiResponse(false, "User not found", null)));
    }
}
