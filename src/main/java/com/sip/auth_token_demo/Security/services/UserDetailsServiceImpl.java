package com.sip.auth_token_demo.Security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sip.auth_token_demo.entity.User;
import com.sip.auth_token_demo.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findUserByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found with username "+ username));
            return UserDetailsImpl.build(user);
    }
    
}
