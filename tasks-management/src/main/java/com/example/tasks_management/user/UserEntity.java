package com.example.tasks_management.user;

import java.util.List;

import com.example.tasks_management.task.TaskEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

@Data
@Entity(name="table_users")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Column(nullable=false)
    private String name;

    @Column(unique=true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    private List<TaskEntity> tasks;
}
