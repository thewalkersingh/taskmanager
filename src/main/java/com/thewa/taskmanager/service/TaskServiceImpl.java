package com.thewa.taskmanager.service;
import com.thewa.taskmanager.exception.TaskNotFoundException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.repositroy.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
  private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
  private final TaskRepository repository;
  
  public TaskServiceImpl(TaskRepository repository) {
	this.repository = repository;
  }
  
  @Override
  public Task createTask(Task task) {
	validateTask(task);
	if(task.getStatus() == null)
	  task.setStatus(Status.PENDING);
	log.info("Creating task: {}", task.getTitle());
	return repository.save(task);
  }
  
  private void validateTask(Task task) {
	if(task.getTitle() == null || task.getTitle().trim().isEmpty()){
	  throw new IllegalArgumentException("Title cannot be empty");
	}
	if(task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())){
	  throw new IllegalArgumentException("Due date cannot be in the past");
	}
	if(task.getPriority() == null){
	  throw new IllegalArgumentException("Priority is required");
	}
  }
  
  @Override
  public Task updateTask(Long id, Task updatedTask) {
	Task existing = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	if(updatedTask.getTitle() != null)
	  existing.setTitle(updatedTask.getTitle());
	if(updatedTask.getDescription() != null)
	  existing.setDescription(updatedTask.getDescription());
	if(updatedTask.getDueDate() != null)
	  existing.setDueDate(updatedTask.getDueDate());
	if(updatedTask.getPriority() != null)
	  existing.setPriority(updatedTask.getPriority());
	if(updatedTask.getStatus() != null)
	  existing.setStatus(updatedTask.getStatus());
	return repository.save(existing);
  }
  
  @Override
  public void deleteTask(Long id) {
	if(repository.findById(id).isEmpty()){
	  log.warn("Task not found: {}", id);
	  throw new TaskNotFoundException(id);
	}
	log.info("Deleting task ID: {}", id);
	repository.deleteById(id);
  }
  
  @Override
  public List<Task> getAllTasks(Status status, Priority priority, LocalDate from, LocalDate to) {
	return repository.findAll().stream().filter(task -> status == null || task.getStatus() == status)
					 .filter(task -> priority == null || task.getPriority() == priority)
					 .filter(task -> from == null ||
									 (task.getDueDate() != null && !task.getDueDate().isBefore(from)))
					 .filter(task -> to == null || (task.getDueDate() != null && !task.getDueDate().isAfter(to)))
					 .collect(Collectors.toList());
  }
  
  @Override
  public Task getTaskById(Long id) {
	return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
  }
  
  @Override
  public List<Task> getFilteredTasks(Status status, Priority priority, String sortBy) {
	Comparator<Task> comparator = Comparator.comparing(Task::getId);
	if("status".equalsIgnoreCase(sortBy)){
	  comparator = Comparator.comparing(Task::getStatus);
	}
	else if("priority".equalsIgnoreCase(sortBy)){
	  comparator = Comparator.comparing(Task::getPriority);
	}
	else if("dueDate".equalsIgnoreCase(sortBy)){
	  comparator = Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
	}
	return repository.findAll().stream()
					 .filter(task -> status == null || task.getStatus() == status)
					 .filter(task -> priority == null || task.getPriority() == priority)
					 .sorted(comparator)
					 .collect(Collectors.toList());
  }
}