package com.thewa.taskmanager.repositroy;
import com.thewa.taskmanager.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
  private final Map<Long, Task> taskStore = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong();
  
  @Override public Task save(Task task) {
	if(task.getId() == null){
	  task.setId(idGenerator.getAndIncrement());
	}
	taskStore.put(task.getId(), task);
	return task;
  }
  
  @Override public Optional<Task> findById(Long id) {
	return Optional.ofNullable(taskStore.get(id));
  }
  
  @Override public void deleteById(Long id) {
	taskStore.remove(id);
  }
  
  @Override public List<Task> findAll() {
	return new ArrayList<>(taskStore.values());
  }
}