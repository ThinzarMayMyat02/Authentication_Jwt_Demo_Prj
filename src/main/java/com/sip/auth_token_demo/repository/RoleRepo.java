package com.sip.auth_token_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sip.auth_token_demo.entity.Role;
import com.sip.auth_token_demo.entity.ERole;


@Repository
public interface RoleRepo extends JpaRepository<Role,Integer>{
    
    Optional<Role> findByName(ERole name);
}
