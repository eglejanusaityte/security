package com.security.security.web.controller;


import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.JwtService;
import com.security.security.business.service.UserService;
import com.security.security.config.JwtAuthenticationFilter;
import com.security.security.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    private final String token = "Bearer someValidJwtToken";
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() throws ServletException, IOException {
        when(jwtService.extractUsername(anyString())).thenReturn("testUser");
        when(jwtService.isAdmin(anyString())).thenReturn(true);

        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllUsers_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        Page<UserDTO> userDTOPage = new PageImpl<>(Collections.singletonList(userDTO));
        when(userService.getUsers(anyInt())).thenReturn(userDTOPage);

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("testUser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");

        when(userService.getUser(anyString())).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/testUser")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(eq(1L), anyString(), anyBoolean());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser_Success() throws Exception {
        UserDAO userDAO = new UserDAO();
        userDAO.setId(1L);
        userDAO.setUsername("updatedUser");

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"username\":\"updatedUser\"}")
                        .header("Authorization", token))
                .andExpect(status().isOk());

        verify(userService).updateUser(any(UserDAO.class), anyString(), anyBoolean());
    }
}
