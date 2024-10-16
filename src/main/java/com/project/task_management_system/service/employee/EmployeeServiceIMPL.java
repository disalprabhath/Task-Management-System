package com.project.task_management_system.service.employee;

import com.project.task_management_system.dto.CommentDTO;
import com.project.task_management_system.dto.TaskDTO;
import com.project.task_management_system.entity.Comment;
import com.project.task_management_system.entity.Task;
import com.project.task_management_system.entity.User;
import com.project.task_management_system.entity.enums.TaskStatus;
import com.project.task_management_system.repository.CommentRepo;
import com.project.task_management_system.repository.TaskRepo;
import com.project.task_management_system.utill.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceIMPL implements EmployeeService{

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    private JwtUtill jwtUtill;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public List<TaskDTO> getTasksByUserId() {
        User user = jwtUtill.getLoggedInUser();
        if(user != null){
            taskRepo.findAllByUserId(user.getId())
                    .stream()
                    .sorted(Comparator.comparing(Task:: getDueDate).reversed())
                    .map(Task :: getTaskDTO)
                    .collect(Collectors.toList());
        }
        throw  new EntityNotFoundException("User Not Found");
    }

    @Override
    public TaskDTO updateTask(int id, String status) {

        Optional<Task> optionalTask= taskRepo.findById(id);
        if (optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRepo.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task Not Found");
    }

    @Override
    public TaskDTO getTaskById(int id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        return optionalTask.map(Task :: getTaskDTO).orElse(null);
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
