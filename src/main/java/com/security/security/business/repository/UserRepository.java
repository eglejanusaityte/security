package com.security.security.business.repository;

import com.security.security.business.repository.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {
    boolean existsByEmail(String email);

    UserDAO findByEmail(String email);
}