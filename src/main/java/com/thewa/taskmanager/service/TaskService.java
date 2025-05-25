package com.thewa.taskmanager.service;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {
  Task createTask(Task task);
  
  Task updateTask(Long id, Task task);
  
  void deleteTask(Long id);
  
  List<Task> getAllTasks(Status status, Priority priority, LocalDate from, LocalDate to, String sortBy);
  
  Optional<Task> getTaskById(Long id);
}