package com.project.task_management_system.service.auth;

import com.project.task_management_system.dto.SignupRequestDTO;
import com.project.task_management_system.dto.UserDTO;
import com.project.task_management_system.entity.User;
import com.project.task_management_system.entity.enums.UserRole;
import com.project.task_management_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService{

    @Autowired
    private UserRepo userRepo;

    @PostConstruct
    public void createAnAdminAccount(){
        Optional<User> optionalUser= userRepo.findByUserRole(UserRole.ADMIN);
        if(optionalUser.isEmpty()){
            User user= new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepo.save(user);
            System.out.println("Admin account created successfully!");
        }else {
            System.out.println("Admin account already exits!");
        }
    }

    @Override
    public UserDTO signupUser(SignupRequestDTO signupRequestDTO) {
        User user= new User();
        user.setEmail(signupRequestDTO.getEmail());
        user.setName(signupRequestDTO.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setUserRole(UserRole.EMPLOYEE);
        User createdUser = userRepo.save(user);
        return createdUser.getUserDTO();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepo.findFirstByEmail(email).isPresent();
    }
}
