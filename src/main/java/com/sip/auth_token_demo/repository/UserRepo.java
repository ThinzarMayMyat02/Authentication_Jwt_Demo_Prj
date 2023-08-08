package com.sip.auth_token_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sip.auth_token_demo.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{
    
    Optional<User> findUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
