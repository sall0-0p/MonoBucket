package com.lordbucket.bucketbank.repository;

import com.lordbucket.bucketbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
}
