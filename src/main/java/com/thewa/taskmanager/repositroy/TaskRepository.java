package com.thewa.taskmanager.repositroy;
import com.thewa.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
  Task save(Task task);
  
  Optional<Task> findById(Long id);
  
  void deleteById(Long id);
  
  List<Task> findAll();
}