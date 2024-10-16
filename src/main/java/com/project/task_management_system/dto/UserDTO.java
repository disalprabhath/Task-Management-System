package com.project.task_management_system.dto;

import com.project.task_management_system.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private int id;

    private String name;

    private String email;

    private String password;

    private UserRole userRole;
}
