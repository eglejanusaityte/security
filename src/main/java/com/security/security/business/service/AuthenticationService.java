package com.security.security.business.service;

import com.security.security.model.User;

import java.io.Serializable;
import java.util.Map;

public interface AuthenticationService {
    Map<String, Serializable> register(User user) throws IllegalArgumentException;
    Map<String, Serializable> login(User user);
}
