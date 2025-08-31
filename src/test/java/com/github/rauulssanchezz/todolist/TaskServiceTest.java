package com.github.rauulssanchezz.todolist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.rauulssanchezz.todolist.exception.ResourceNotFoundException;
import com.github.rauulssanchezz.todolist.models.Task;
import com.github.rauulssanchezz.todolist.repositories.TaskRepository;
import com.github.rauulssanchezz.todolist.services.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    
    @Test
    void whenUpdateTask_thenTaskIsUpdated() {
        // Given
        Task updatedTask = new Task("Updated title", "Updated description", true);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Task> result = taskService.updateTask(1L, updatedTask);

        // Then
        assertNotNull(result);
        assertEquals("Updated title", result.get().getTitle());
        assertEquals("Updated description", result.get().getDescription());
        assertEquals(true, result.get().getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void whenUpdateTaskAndTaskNotExists_thenResourceNotFoundExceptionIsThrown() {
        // Given
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(1L, testTask));
    }

    final String TASK_TITLE = "Test task";
	
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        //Given
        this.testTask = new Task(TASK_TITLE, "Description", false);
    }

    @Test
    void whenGetTaskById_thenTaskIsFound() {

        //Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        //When
        Task foundTask = taskService.getTaskById(1L);

        //Then
        assertNotNull(foundTask, "foundTask should be not null");
        assertEquals(TASK_TITLE, foundTask.getTitle());

    }

    @Test
    void whenGetTaskById_thenResourceNotFoundExceptionIsThrown() {
        
        //Given
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Then
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));

    }

    @Test
    void whenGetAllTasks_thenTasksAreFound() {

        //Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(testTask);
        testTask.setTitle("Test task 2");
        taskList.add(testTask);

        when(taskRepository.findAll()).thenReturn(taskList);

        //When 
        List<Task> foundsTask = taskService.getAllTask();

        //Then
        assertNotNull(foundsTask);
        assertEquals(taskList, foundsTask);

    }

    @Test
    void whenCreateTask_thenTaskIsSaved() {
        // Given
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // When
        Task createdTask = taskService.createTask(testTask);

        // Then
        assertNotNull(createdTask);
        assertEquals(TASK_TITLE, createdTask.getTitle());
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void whenDeleteTask_thenTaskIsDeleted() {

        // Given
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyLong());
        
        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).deleteById(1L);

    }

    @Test
    void whenDeleteTaskAndTaskNotExists_thenResourceNotFoundExceptionIsThrow() {

        // Given
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        // Then
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));

    }

}
