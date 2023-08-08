// package com.sip.auth_token_demo.configuration;

// import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.event.ContextRefreshedEvent;
// import org.springframework.context.event.EventListener;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.transaction.annotation.Transactional;

// import com.sip.auth_token_demo.entity.ERole;
// import com.sip.auth_token_demo.entity.Role;
// import com.sip.auth_token_demo.entity.User;
// import com.sip.auth_token_demo.repository.UserRepo;

// @Configuration
// @Transactional
// public class AdminUserInitializer {
//     @Value("${app.user.admin.name}")
//     private String name;
//     @Value("${app.user.admin.email}")
//     private String email;
//     @Value("${app.user.admin.password}")
//     private String password;

//     @Autowired
//     private UserRepo userRepository;
//     @Autowired
//     private PasswordEncoder passwordEncoder;
    
//     @EventListener(value = ContextRefreshedEvent.class)
//     public void execute(){
//         if(userRepository.count() == 0L){
//             User entity=new User();
//             entity.setUsername(name);
//             entity.setEmail(email);
//             entity.setPassword(passwordEncoder.encode(password));
//             entity.setRoles(new Role(ERole.ADMIN));
//         }
//     }
// }
