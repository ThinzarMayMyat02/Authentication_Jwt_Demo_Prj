package com.sip.auth_token_demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.auth_token_demo.Security.jwt.JwtUtils;
import com.sip.auth_token_demo.Security.services.UserDetailsImpl;
import com.sip.auth_token_demo.entity.ERole;
import com.sip.auth_token_demo.entity.Role;
import com.sip.auth_token_demo.entity.User;
import com.sip.auth_token_demo.payload.request.LoginRequest;
import com.sip.auth_token_demo.payload.request.SignupRequest;
import com.sip.auth_token_demo.payload.response.JwtResponse;
import com.sip.auth_token_demo.payload.response.MessageResponse;
import com.sip.auth_token_demo.repository.RoleRepo;
import com.sip.auth_token_demo.repository.UserRepo;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepository;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest()
                            .body(new MessageResponse("Error: Username is already exist."));
        }
        
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Email is already exists."));
        }

        User user=new User( signupRequest.getUsername(),
                            signupRequest.getEmail(), 
                            passwordEncoder.encode(signupRequest.getPassword()));

            Set<String> strRoles= signupRequest.getRole();
            Set<Role> roles=new HashSet<>();

            if(strRoles==null){
                Role userRole=roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                    roles.add(userRole);
            }else{
                strRoles.forEach(rol->{
                    switch (rol) {
                        case "superadmin":
                            Role supuerRole=roleRepository.findByName(ERole.ROLE_SUPERADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                            roles.add(supuerRole);
                            break;

                        case "admin":
                            Role adminRole=roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                            roles.add(adminRole);           
                            break;
                    
                        case "developer":
                            Role devRole=roleRepository.findByName(ERole.ROLE_DEVELOPER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                            roles.add(devRole);
                            break;

                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
    }
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetailsImpl=(UserDetailsImpl) authentication.getPrincipal();
        List<String> roles=userDetailsImpl.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                    userDetailsImpl.getId(), 
                                    userDetailsImpl.getUsername(), 
                                    userDetailsImpl.getEmail(), roles));
        }
}
