package com.github.rauulssanchezz.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rauulssanchezz.todolist.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{}
