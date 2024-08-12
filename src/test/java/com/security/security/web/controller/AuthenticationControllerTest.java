package com.security.security.web.controller;

import com.security.security.business.service.AuthenticationService;
import com.security.security.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@email.com");
        user.setPassword("password");
    }

    @Test
    void register_Success() throws Exception {
        when(authenticationService.register(any(User.class))).thenReturn(Map.of("token", "token"));
        String userJson = "{\n" +
                "  \"username\": \"testUser\",\n" +
                "  \"email\": \"testUser@email.com\",\n" +
                "  \"password\": \"password\",\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"token\"}"));
        ;
    }

    @Test
    void login_Success() throws Exception {
        when(authenticationService.login(any(User.class))).thenReturn(Map.of("token", "token"));

        String userJson = "{\n" +
                "  \"username\": \"testUser\",\n" +
                "  \"password\": \"password\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"token\"}"));
    }
}
