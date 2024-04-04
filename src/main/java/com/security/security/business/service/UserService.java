package com.security.security.business.service;

import com.security.security.model.User;

public interface UserService {

    User createUser(User user);
    String hashPassword(String password);
}
