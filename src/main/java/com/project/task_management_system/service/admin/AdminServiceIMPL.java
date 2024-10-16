package com.project.task_management_system.service.admin;

import com.project.task_management_system.dto.CommentDTO;
import com.project.task_management_system.dto.TaskDTO;
import com.project.task_management_system.dto.UserDTO;
import com.project.task_management_system.entity.Comment;
import com.project.task_management_system.entity.Task;
import com.project.task_management_system.entity.User;
import com.project.task_management_system.entity.enums.TaskStatus;
import com.project.task_management_system.entity.enums.UserRole;
import com.project.task_management_system.repository.CommentRepo;
import com.project.task_management_system.repository.TaskRepo;
import com.project.task_management_system.repository.UserRepo;
import com.project.task_management_system.utill.JwtUtill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceIMPL implements AdminService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private JwtUtill jwtUtill;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public List<UserDTO> getUsers() {
        return userRepo.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User :: getUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<User> optionalUser=userRepo.findById(taskDTO.getEmployeeId());
        if(optionalUser.isPresent()){
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            return taskRepo.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(int id) {
        taskRepo.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(int id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        return optionalTask.map(Task :: getTaskDTO).orElse(null);
    }

    @Override
    public TaskDTO updateTask(int id, TaskDTO taskDTO) {
        Optional<Task> optionalTask=taskRepo.findById(id);
        Optional<User> optionalUser=userRepo.findById(taskDTO.getEmployeeId());
        if (optionalTask.isPresent()){
            Task exitingTask=optionalTask.get();
            exitingTask.setTitle(taskDTO.getTitle());
            exitingTask.setDescription(taskDTO.getDescription());
            exitingTask.setDueDate(taskDTO.getDueDate());
            exitingTask.setPriority(taskDTO.getPriority());
            exitingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
            exitingTask.setUser(optionalUser.get());
            return taskRepo.save(exitingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
        return taskRepo.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task :: getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> searchTaskByUserId(int id) {
        return taskRepo.findAllByUserId(id)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task :: getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(int taskId, String content) {
        Optional<Task> optionalTask=taskRepo.findById(taskId);
        User user=jwtUtill.getLoggedInUser();
        if((optionalTask.isPresent()) && user != null){
            Comment comment=new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepo.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("User Or Task Not Found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(int taskId) {
        return commentRepo.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDTO)
                .collect(Collectors.toList());
    }

    private TaskStatus mapStringToTaskStatus(String status){
        return switch (status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }


}
