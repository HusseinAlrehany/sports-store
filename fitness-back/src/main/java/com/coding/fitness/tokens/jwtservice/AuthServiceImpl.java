package com.coding.fitness.tokens.jwtservice;

import com.coding.fitness.dtos.SignUpRequest;
import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.enums.UserRole;
import com.coding.fitness.mapper.UserMapper;
import com.coding.fitness.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserMapper userMapper;

    //to create admin account
    @PostConstruct
    public void createAdminAccount(){
        User adminUser = userRepository.findByRole(UserRole.ADMIN);
        if(null == adminUser){
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setName("admin");
            admin.setRole(UserRole.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));

            userRepository.save(admin);
        }
    }

    @Override
    public UserDTO signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setRole(UserRole.CUSTOMER);
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));

        User dbUser = userRepository.save(user);

        return userMapper.toDTO(dbUser);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
