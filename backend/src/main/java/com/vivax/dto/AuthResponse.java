package com.vivax.dto;
import com.vivax.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
