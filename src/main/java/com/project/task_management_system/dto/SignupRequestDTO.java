package com.project.task_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDTO {

    private String name;

    private String email;

    private String password;
}
