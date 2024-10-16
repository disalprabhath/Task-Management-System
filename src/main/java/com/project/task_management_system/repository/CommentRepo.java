package com.project.task_management_system.repository;

import com.project.task_management_system.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CommentRepo  extends JpaRepository<Comment,Integer> {

    List<Comment> findAllByTaskId(int taskId);
}
