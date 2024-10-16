package com.project.task_management_system.repository;

import com.project.task_management_system.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
    List<Task> findAllByTitleContaining(String title);

    List<Task> findAllByUserId(int id);
}
