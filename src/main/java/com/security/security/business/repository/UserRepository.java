package com.security.security.business.repository;

import com.security.security.business.repository.model.UserDAO;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserDAO, Long> {
    UserDAO findByUsername(String username);

    void delete(UserDAO userDAO);

    UserDAO save(UserDAO userDAO);

    Optional<UserDAO> findById(Long id);
}