package com.security.security.business.service.impl;

import com.security.security.business.enums.Role;
import com.security.security.business.mappers.UserMapper;
import com.security.security.business.repository.UserRepository;
import com.security.security.business.repository.model.UserDAO;
import com.security.security.business.service.AuthenticationService;
import com.security.security.business.service.JwtService;
import com.security.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public Map<String, Serializable> register(User user) throws IllegalArgumentException {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        UserDAO userDAO = UserMapper.INSTANCE.userToUserDAO(user);
        userDAO.setRole(Role.USER);
        userDAO = userRepository.save(userDAO);
        User finalUser = UserMapper.INSTANCE.userDAOToUser(userDAO);

        String token = jwtService.generateToken(finalUser);
        return Map.of("token", token);
    }

    public Map<String, Serializable> login(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDAO userDAO = userRepository.findByUsername(user.getUsername());
        user = UserMapper.INSTANCE.userDAOToUser(userDAO);
        String token = jwtService.generateToken(user);
        return Map.of("token", token);
    }

}
