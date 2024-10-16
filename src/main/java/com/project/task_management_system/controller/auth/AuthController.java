package com.project.task_management_system.controller.auth;

import com.project.task_management_system.dto.AuthenticationRequestDTO;
import com.project.task_management_system.dto.AuthenticationResponseDTO;
import com.project.task_management_system.dto.SignupRequestDTO;
import com.project.task_management_system.dto.UserDTO;
import com.project.task_management_system.entity.User;
import com.project.task_management_system.repository.UserRepo;
import com.project.task_management_system.service.auth.AuthService;
import com.project.task_management_system.service.jwt.UserService;
import com.project.task_management_system.utill.JwtUtill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtill jwtUtill;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDTO signupRequestDTO){
        if(authService.hasUserWithEmail(signupRequestDTO.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exist with this email");
        UserDTO createdUserDTO = authService.signupUser(signupRequestDTO);
        if(createdUserDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getEmail(),
                    authenticationRequestDTO.getPassword()));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect email or password");
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequestDTO.getEmail());

        Optional<User> optionalUser = userRepo.findFirstByEmail(authenticationRequestDTO.getEmail());

        final String jwtToken = jwtUtill.generateToken(userDetails);

        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        if (optionalUser.isPresent()){
            authenticationResponseDTO.setJwt(jwtToken);
            authenticationResponseDTO.setUserId(optionalUser.get().getId());
            authenticationResponseDTO.setUserRole(optionalUser.get().getUserRole());
        }
        return ResponseEntity.ok(authenticationResponseDTO);
    }

}
