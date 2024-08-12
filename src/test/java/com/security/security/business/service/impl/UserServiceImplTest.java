package com.security.security.business.service.impl;

import com.security.security.business.enums.Role;
import com.security.security.business.mappers.UserMapper;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.dto.UserDTO;
import com.security.security.model.User;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserDAO userDAO;
    private UserDTO userDTO;
    private UserDAO updatedUserDAO;

    public UserServiceImplTest() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("testUser@email.com");
        user.setRole(Role.USER);

        userDAO = new UserDAO();
        userDAO.setId(1L);
        userDAO.setUsername("testUser");
        userDAO.setPassword("password");
        userDAO.setEmail("testUser@email.com");
        userDAO.setRole(Role.USER);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setEmail("testUser@email.com");
        userDTO.setRole(Role.USER.name());

        updatedUserDAO = new UserDAO();
        updatedUserDAO.setId(1L);
        updatedUserDAO.setUsername("testUser");
        updatedUserDAO.setPassword("updatedPassword");
        updatedUserDAO.setEmail("testUser@email.com");
        updatedUserDAO.setRole(Role.USER);
    }

    @Test
    void getUsers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDAO> userDAOPage = new PageImpl<>(Collections.singletonList(userDAO), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(userDAOPage);
        when(userMapper.userDAOToUserDTO(userDAO)).thenReturn(userDTO);

        Page<UserDTO> result = userService.getUsers(0);

        assertEquals(1, result.getTotalElements());
        assertEquals(userDTO, result.getContent().get(0));
        verify(userRepository).findAll(pageable);
    }

    @Test
    void deleteUser_Success() throws AccessDeniedException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        userService.deleteUser(1L, "testUser", true);
        verify(userRepository).delete(userDAO);
    }

    @Test
    void deleteUser_NoPermission() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () -> {
            userService.deleteUser(1L, "otherUser", false);
        });
        assertEquals("You do not have permission to delete this user.", thrown.getMessage());
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L, "testUser", true);
        });

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void updateUser_Success() throws AccessDeniedException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        when(userMapper.userDAOToUser(any(UserDAO.class))).thenReturn(user);
        when(passwordEncoder.encode("updatedPassword")).thenReturn("hashedPassword");

        User result = userService.updateUser(updatedUserDAO, "testUser", true);

        assertEquals(user, result);
        verify(userRepository).save(updatedUserDAO);
    }

    @Test
    void updateUserOldPassword_Success() throws AccessDeniedException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        when(userMapper.userDAOToUser(any(UserDAO.class))).thenReturn(user);
        updatedUserDAO.setPassword("");

        User result = userService.updateUser(updatedUserDAO, "testUser", true);

        assertEquals(user, result);
        verify(userRepository).save(updatedUserDAO);
    }

    @Test
    void updateUser_NoPermission() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () -> {
            userService.updateUser(updatedUserDAO, "otherUser", false);
        });

        assertEquals("You do not have permission to delete this user.", thrown.getMessage());
    }

    @Test
    void updateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(updatedUserDAO, "testUser", true);
        });

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void getUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(userDAO);
        when(userMapper.userDAOToUserDTO(userDAO)).thenReturn(userDTO);

        UserDTO result = userService.getUser("testUser");

        assertEquals(userDTO, result);
        verify(userRepository).findByUsername("testUser");
    }

    @Test
    void getUser_NotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            userService.getUser("testUser");
        });

        assertEquals("User with username testUser not found.", thrown.getMessage());
    }
}
