package com.security.security.business.service.impl;

import com.security.security.business.mappers.UserMapper;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.UserService;
import com.security.security.dto.UserDTO;
import com.security.security.model.User;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Page<UserDTO> getUsers(Integer page) {
        Pageable userPage = PageRequest.of(page, 10);
        return userRepository.findAll(userPage).map(userMapper::userDAOToUserDTO);
    }

    @Override
    public void deleteUser(Long id, String username, boolean is_admin) throws AccessDeniedException {
        UserDAO userDAO = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(userDAO.getUsername().equals(username) || is_admin) {
            userRepository.delete(userDAO);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }
    }

    @Override
    public User updateUser(UserDAO userDAO, String username, boolean is_admin) throws AccessDeniedException {
        UserDAO userDAOFound = userRepository.findById(userDAO.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!userDAO.getPassword().isEmpty()){
            String hashedPassword = passwordEncoder.encode(userDAO.getPassword());
            userDAO.setPassword(hashedPassword);
        } else {
            userDAO.setPassword(userDAOFound.getPassword());
        }
        if(userDAO.getUsername().equals(username) || is_admin){
            userRepository.save(userDAO);
            return userMapper.userDAOToUser(userDAO);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this user.");
        }
    }

    @Override
    public UserDTO getUser(String username) {
        UserDAO userDAO = userRepository.findByUsername(username);
        if (userDAO == null) {
            throw new NoSuchElementException("User with username " + username + " not found.");
        }
        return userMapper.userDAOToUserDTO(userDAO);
    }

}
