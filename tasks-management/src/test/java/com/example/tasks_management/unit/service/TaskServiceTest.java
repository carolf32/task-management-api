package com.example.tasks_management.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.security.AuthenticatedUser;
import com.example.tasks_management.task.TaskEntity;
import com.example.tasks_management.task.TaskRepository;
import com.example.tasks_management.task.TaskService;
import com.example.tasks_management.task.dtos.TaskCreateDTO;
import com.example.tasks_management.user.UserEntity;
import com.example.tasks_management.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testCreateTask_Success(){
        UserEntity mockUser = UserEntity.builder().id(1L).build();
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTask("New Task");
        dto.setDaysUntilDeadline(3);

        TaskEntity task = TaskEntity.builder()
                .task(dto.getTask())
                .daysUntilDeadline(dto.getDaysUntilDeadline())
                .completed(false)
                .user(mockUser)
                .build();

        when(authenticatedUser.getAuthenticatedUser()).thenReturn(mockUser);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);
        
        TaskEntity createdTask = taskService.createTask(dto);

        assertEquals("New Task", createdTask.getTask());
        assertEquals(3, createdTask.getDaysUntilDeadline());
        assertFalse(createdTask.isCompleted());
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void testGetTaskById_Success(){
        TaskEntity mockTask = TaskEntity.builder().id(1L).task("Existing Task").build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        TaskEntity foundTask = taskService.getTaskById(1L);

        assertEquals(1L, foundTask.getId());
        assertEquals("Existing Task", foundTask.getTask());
    }

    @Test
    public void testGetTaskById_NotFound(){
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(1L), "Task not found");
    }

    @Test
    public void testGetTasksByLoggedUser_Success(){
        UserEntity mockUser = UserEntity.builder().id(1L).build();
        TaskEntity mockTask1 = TaskEntity.builder().id(1L).user(mockUser).task("Task 1").build();
        TaskEntity mockTask2 = TaskEntity.builder().id(1L).user(mockUser).task("Task 2").build();

        when(authenticatedUser.getAuthenticatedUser()).thenReturn(mockUser);
        when(taskRepository.findByUser(mockUser)).thenReturn(List.of(mockTask1, mockTask2));

        List<TaskEntity> tasks = taskService.getTasksByUserId();

        assertEquals(2, tasks.size());
        verify(taskRepository).findByUser(mockUser);
    }

   @Test
   public void testGetTasksBySpecificUser_Success(){
    UserEntity mockUser = UserEntity.builder().id(1L).build();
    TaskEntity mockTask1 = TaskEntity.builder().id(1L).user(mockUser).task("Task 1").build();
    TaskEntity mockTask2 = TaskEntity.builder().id(1L).user(mockUser).task("Task 2").build();
   
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
    when(taskRepository.findByUser(mockUser)).thenReturn(List.of(mockTask1, mockTask2));

    List<TaskEntity> tasks = taskService.getTasksByUserId(1L);

    assertEquals(2, tasks.size());
    verify(userRepository).findById(1L );
   }

   @Test
    public void testGetTasksBySpecificUser_UserNotFound(){
        Long nonExistingUserId = 1L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTasksByUserId(nonExistingUserId), "User not found");
    }

    @Test
    public void testGetTasksByStatus(){
        boolean completedStatus = true;
        TaskEntity completedTask = TaskEntity.builder().completed(completedStatus).build();
        when(taskRepository.findByCompleted(completedStatus)).thenReturn(List.of(completedTask));

        List<TaskEntity> tasks = taskService.getTasksByStatus(completedStatus);

        assertEquals(1, tasks.size());
        assertEquals(completedStatus, tasks.get(0).isCompleted());
    }

    @Test
    public void testGetTasksByTitle(){
        String title = "Test Task";
        TaskEntity task = TaskEntity.builder().task(title).build();

        when(taskRepository.findByTaskTitle(title)).thenReturn(List.of(task));

        List<TaskEntity> tasks = taskService.searchTasksByTitle(title);

        assertEquals(1, tasks.size());
        assertEquals(title, tasks.get(0).getTask());
    }

    @Test
    public void testFindByDaysUntilDeadline(){
        TaskEntity task1 = TaskEntity.builder().daysUntilDeadline(9).build();
        TaskEntity task2 = TaskEntity.builder().daysUntilDeadline(9).build();

        when(taskRepository.findByDaysUntilDeadline(9)).thenReturn(List.of(task1, task2));

        List<TaskEntity> tasks = taskService.findByDaysUntilDeadline(9);

        assertEquals(2, tasks.size());
        assertEquals(9, tasks.get(0).getDaysUntilDeadline());
    }

    @Test
    public void testDeleteTask_Success(){
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.deleteTask(1L);
        verify(taskRepository).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_NotFound(){
        when(taskRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1L), "Task not found");
        verify(taskRepository, never()).deleteTask(1L);
    }

    @Test
    public void testUpdateTask_Success(){
        TaskEntity existingTask = TaskEntity.builder()
                .id(1L)
                .task("Old Task")
                .daysUntilDeadline(5)
                .completed(false)
                .build();
        
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTask("Updated Task");
        dto.setDaysUntilDeadline(2);
        dto.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        TaskEntity updatedTask = taskService.updateTask(1L, dto);

        assertEquals("Updated Task", updatedTask.getTask());
        assertEquals(2, updatedTask.getDaysUntilDeadline());
        assertEquals(true, updatedTask.isCompleted());
    }

    @Test
    public void testUpdateTask_NotFound(){
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.updateTask(1L, new TaskCreateDTO()), "Task not found");
    }
}
