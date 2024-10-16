package com.project.task_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.task_management_system.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    public CommentDTO getCommentDTO(){
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setContent(content);
        commentDTO.setCreatedAt(createdAt);
        commentDTO.setTaskId(task.getId());
        commentDTO.setPostedBy(user.getName());
        return commentDTO;
    }
}
