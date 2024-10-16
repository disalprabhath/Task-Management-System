package com.project.task_management_system.service.auth;

import com.project.task_management_system.dto.SignupRequestDTO;
import com.project.task_management_system.dto.UserDTO;

public interface AuthService {

    UserDTO signupUser(SignupRequestDTO signupRequestDTO);

    boolean hasUserWithEmail(String email);
}
