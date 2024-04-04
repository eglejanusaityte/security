package com.security.security.business.service.impl;

import com.security.security.business.enums.Role;
import com.security.security.business.mappers.UserMapper;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.UserService;
import com.security.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws IllegalArgumentException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists.");
        }
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        UserDAO userDAO = UserMapper.INSTANCE.userToUserDAO(user);
        userDAO.setRole(Role.USER);
        userDAO = userRepository.save(userDAO);

        return UserMapper.INSTANCE.userDAOToUser(userDAO);
    }

    public String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
