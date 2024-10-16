package com.project.task_management_system.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private int id;

    private String content;

    private Date createdAt;

    private int taskId;

    private int userId;

    private String postedBy;


}
