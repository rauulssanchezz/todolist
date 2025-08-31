package com.github.rauulssanchezz.todolist.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rauulssanchezz.todolist.repositories.TaskRepository;
import com.github.rauulssanchezz.todolist.models.Task;
import com.github.rauulssanchezz.todolist.exception.ResourceNotFoundException;

@Service
public class TaskService {

    final String TASK_NOT_FOUND = "Tarea no encontrada con id: ";

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTaks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND + id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND + id));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setCompleted(taskDetails.isCompleted());
        existingTask.setDescription(taskDetails.getDescription());

        return Optional.of(taskRepository.save(existingTask));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(TASK_NOT_FOUND + id);
        }

        taskRepository.deleteById(id);
    }

}
