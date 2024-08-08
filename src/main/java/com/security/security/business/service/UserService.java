package com.security.security.business.service;

import com.security.security.business.repository.model.UserDAO;
import com.security.security.dto.UserDTO;
import com.security.security.model.User;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    Page<UserDTO> getUsers(Integer page);
    void deleteUser(Long id, String username, boolean is_admin) throws AccessDeniedException;
    User updateUser(UserDAO userDAO, String username, boolean is_admin) throws AccessDeniedException;
    UserDTO getUser(String username);
}
