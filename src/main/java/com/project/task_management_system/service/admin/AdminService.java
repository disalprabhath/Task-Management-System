package com.project.task_management_system.service.admin;

import com.project.task_management_system.dto.CommentDTO;
import com.project.task_management_system.dto.TaskDTO;
import com.project.task_management_system.dto.UserDTO;

import java.util.List;

public interface AdminService {

    List<UserDTO> getUsers();

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasks();

    void deleteTask(int id);

    TaskDTO getTaskById(int id);

    TaskDTO updateTask(int id, TaskDTO taskDTO);

    List<TaskDTO> searchTaskByTitle(String title);

    List<TaskDTO> searchTaskByUserId(int id);

    CommentDTO createComment(int taskId, String content);

    List<CommentDTO> getCommentsByTaskId(int taskId);
}
