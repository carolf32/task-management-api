package com.example.tasks_management.task;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasks_management.task.dtos.TaskCreateDTO;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public TaskEntity createTask(@Valid @RequestBody TaskCreateDTO dto){
        return taskService.createTask(dto);
    }

    @GetMapping
    public List<TaskEntity> getAllTasks(){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskEntity getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @GetMapping("/users/{userId}")
    public List<TaskEntity> getTasksByUserId(@PathVariable Long userId){
        return taskService.getTasksByUserId(userId);
    }

    @GetMapping("/status/{completed}")
    public List<TaskEntity> getTasksByStatus(@PathVariable boolean completed){
        return taskService.getTasksByStatus(completed);
    }

    @GetMapping("/search")
    public List<TaskEntity> getTasksByTitle(@RequestParam String task){
        return taskService.searchTasksByTitle(task);
    }

    @GetMapping("/status/{days}")
    public List<TaskEntity> getTasksByDaysUntilDeadline(@PathVariable int days){
        return taskService.findByDaysUntilDeadline(days);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public TaskEntity updateTask(@PathVariable Long id, @Valid @RequestBody TaskCreateDTO dto){
        return taskService.updateTask(id, dto);
    }
}
