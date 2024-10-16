package com.project.task_management_system.controller.employee;

import com.project.task_management_system.dto.CommentDTO;
import com.project.task_management_system.dto.TaskDTO;
import com.project.task_management_system.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(){
        return ResponseEntity.ok(employeeService.getTasksByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable int id, @PathVariable String status){
        TaskDTO updatedTaskDTO=employeeService.updateTask(id,status);
        if(updatedTaskDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable int id){
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable int taskId, @RequestParam String content){
        CommentDTO createdCommentDto=employeeService.createComment(taskId,content);
        if(createdCommentDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }

    @GetMapping("comments/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable int taskId){
        return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
    }
}
