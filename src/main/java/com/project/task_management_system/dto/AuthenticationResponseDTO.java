package com.project.task_management_system.dto;

import com.project.task_management_system.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponseDTO {

    private String jwt;

    private int userId;

    private UserRole userRole;
}
