package com.security.security.business.service.impl;

import com.security.security.business.enums.Role;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.JwtService;
import com.security.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void register_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("testUser@email.com");
        user.setRole(Role.USER);

        String hashedPassword = "hashedPassword";
        String token = "generatedToken";

        UserDAO userDAO = new UserDAO();
        userDAO.setUsername(user.getUsername());
        userDAO.setRole(user.getRole());
        userDAO.setEmail(user.getEmail());
        userDAO.setPassword(hashedPassword);

        when(passwordEncoder.encode(user.getPassword())).thenReturn(hashedPassword);
        when(userRepository.save(any(UserDAO.class))).thenReturn(userDAO);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);

        Map<String, Serializable> response = authenticationService.register(user);

        assertEquals(token, response.get("token"));
    }

    @Test
    void login_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("testUser@email.com");
        user.setRole(Role.USER);

        String hashedPassword = "hashedPassword";
        String token = "generatedToken";

        UserDAO userDAO = new UserDAO();
        userDAO.setUsername(user.getUsername());
        userDAO.setRole(user.getRole());
        userDAO.setEmail(user.getEmail());
        userDAO.setPassword(hashedPassword);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(userDAO);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);

        Map<String, Serializable> response = authenticationService.login(user);

        assertEquals(token, response.get("token"));
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }
}
