package com.example.tasks_management.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.security.AuthenticatedUser;
import com.example.tasks_management.task.dtos.TaskCreateDTO;
import com.example.tasks_management.user.UserEntity;
import com.example.tasks_management.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TaskEntity createTask(TaskCreateDTO dto){
        UserEntity loggedUser = AuthenticatedUser.getAuthenticatedUser();

        TaskEntity task = TaskEntity.builder()
        .task(dto.getTask())
        .daysUntilDeadline(dto.getDaysUntilDeadline())
        .completed(false)
        .user(loggedUser)
        .build();

        return taskRepository.save(task);
    }

    public List<TaskEntity> getAllTasks(){
        return taskRepository.findAll();
    }

    public TaskEntity getTaskById(Long id){
        return taskRepository.findById(id)
        .orElseThrow(()->new NotFoundException("Task not found"));
    }

    public List<TaskEntity> getTasksByUserId(){
        UserEntity loggedUser = AuthenticatedUser.getAuthenticatedUser();

        return taskRepository.findByUser(loggedUser);
    }

    public List<TaskEntity> getTasksByUserId(Long id){
        UserEntity user = userRepository.findById(id)
        .orElseThrow(()->new NotFoundException("User not found"));

        return taskRepository.findByUser(user);
    }

    public List<TaskEntity> getTasksByStatus(boolean completed){
        return taskRepository.findByCompleted(completed);
    }

    public List<TaskEntity> searchTasksByTitle(String title) {
        return taskRepository.findByTaskTitle(title);
    }

    public List<TaskEntity> findByDaysUntilDeadline(int days){
        return taskRepository.findByDaysUntilDeadline(days);
    }

    public void deleteTask(Long id){
        if(!taskRepository.existsById(id)){
            throw new NotFoundException("Task was not found");
        }

        taskRepository.deleteTask(id);
    }

    @Transactional
    public TaskEntity updateTask(Long id, TaskCreateDTO dto){
        TaskEntity task = taskRepository.findById(id)
        .orElseThrow(()->new NotFoundException("Task not found"));

        task.setTask(dto.getTask());
        task.setDaysUntilDeadline(dto.getDaysUntilDeadline());
        task.setCompleted(dto.isCompleted());

        return task;
    }
}
