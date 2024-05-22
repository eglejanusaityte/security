package com.security.security.business.service;

import com.security.security.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);
    String generateToken(User userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isAdmin(String token);

}
