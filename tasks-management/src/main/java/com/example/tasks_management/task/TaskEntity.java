package com.example.tasks_management.task;

import com.example.tasks_management.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Entity(name="table_tasks")
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false)
    private String task;

    private int daysUntilDeadline;
    
    private boolean completed;

    @ManyToOne
    @JoinColumn(name="task_id")
    private UserEntity user;
}
