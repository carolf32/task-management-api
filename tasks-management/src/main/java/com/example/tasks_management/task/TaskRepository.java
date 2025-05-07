package com.example.tasks_management.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tasks_management.user.UserEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUser(UserEntity user);

    List<TaskEntity> findByCompleted(boolean completed);

    List<TaskEntity> findByTaskTitle(String title);

    List<TaskEntity> findByDaysUntilDeadline(int days);

    void deleteTask(Long id);

    TaskEntity updateEntity(TaskEntity payload);
}
