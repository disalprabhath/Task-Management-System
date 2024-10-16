package com.project.task_management_system.service.employee;

import com.project.task_management_system.dto.CommentDTO;
import com.project.task_management_system.dto.TaskDTO;

import java.util.List;

public interface EmployeeService {

    List<TaskDTO> getTasksByUserId();

    TaskDTO updateTask(int id, String status);

    TaskDTO getTaskById(int id);

    CommentDTO createComment(int taskId, String content);

    List<CommentDTO> getCommentsByTaskId(int taskId);
}
