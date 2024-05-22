package com.security.security.business.service.Impl;

import com.security.security.business.enums.Role;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.JwtService;
import com.security.security.business.service.impl.AuthenticationServiceImpl;
import com.security.security.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

//    @Test
//    void testRegister() {
//        User user = new User(1L, "test@example.com", "password", Role.USER);
//
//        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
//
//        UserDAO userDAO = new UserDAO(1L, "test@example.com", "password", Role.USER);
//        when(userRepository.save(any(UserDAO.class))).thenReturn(userDAO);
//
//        when(jwtService.generateToken(any(User.class))).thenReturn("sampleJwtToken");
//
//        Map<String, Serializable> response = authenticationService.register(user);
//
//        assertEquals(1, response.size());
//        assertEquals("sampleJwtToken", response.get("token"));
//    }
//
//    @Test
//    void testLogin() {
//        User user = new User(1L, "test@example.com", "password", Role.USER);
//        UserDAO userDAO = new UserDAO(1L, "test@example.com", "password", Role.USER);
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(userDAO);
//
//        when(jwtService.generateToken(any(User.class))).thenReturn("sampleJwtToken");
//
//        Map<String, Serializable> response = authenticationService.login(user);
//
//        assertEquals(1, response.size());
//        assertEquals("sampleJwtToken", response.get("token"));
//    }
}