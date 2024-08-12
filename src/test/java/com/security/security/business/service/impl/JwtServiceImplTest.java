package com.security.security.business.service.impl;

import com.security.security.business.enums.Role;
import com.security.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private final String SECRET_KEY = "PKxJDBLi6tYzUFZDLC9ZLewlWdmKmmqByc5TGXOOttF0YXuMxqhuC7iLBH1zF2QDon8q4Ce8KPkF4sS8bfZYmg";

    private final long jwtExpiration = 604800000;

    @Mock
    private UserDetails userDetails;

    private String token;
    private User user;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        Field secretKeyField = JwtServiceImpl.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, SECRET_KEY);

        Field jwtExpirationField = JwtServiceImpl.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(jwtService, jwtExpiration);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("testUser@email.com");
        user.setRole(Role.USER);
        token = jwtService.generateToken(user);
    }

    @Test
    void extractUsername_Success() {
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void isAdmin_True() {
        user.setRole(Role.ADMIN);
        String adminToken = jwtService.generateToken(user);

        boolean isAdmin = jwtService.isAdmin(adminToken);
        assertTrue(isAdmin);
    }

    @Test
    void isAdmin_False() {
        boolean isAdmin = jwtService.isAdmin(token);
        assertFalse(isAdmin);
    }

    @Test
    void extractClaim_Success() {
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertEquals("USER", role);
    }

    @Test
    void generateToken_Success() {
        String generatedToken = jwtService.generateToken(user);
        assertNotNull(generatedToken);
        assertEquals(token, generatedToken);
    }

    @Test
    void isTokenValid_Valid() {
        when(userDetails.getUsername()).thenReturn("testUser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_Invalid() {
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertFalse(isValid);
    }
}
