package com.thewa.taskmanager.service;
import com.thewa.taskmanager.exception.TaskNotFoundException;
import com.thewa.taskmanager.exception.TaskValidationException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
  private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
  private static final Map<String, Comparator<Task>> SORT_OPTIONS = Map.of(
		  "status", Comparator.comparing(Task::getStatus),
		  "priority", Comparator.comparing(Task::getPriority),
		  "duedate", Comparator.comparing(Task::getDueDate,
				  Comparator.nullsLast(Comparator.naturalOrder())));
  private final TaskRepository repository;
  
  public TaskServiceImpl(TaskRepository repository) {
	this.repository = repository;
  }
  
  @Override
  public Task createTask(Task task) {
	validateTask(task);
	if(task.getStatus() == null){
	  task.setStatus(Status.PENDING);
	}
	log.info("Creating task: {}", task.getTitle());
	return repository.save(task);
  }
  
  private void validateTask(Task task) {
	if(task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())){
	  throw new TaskValidationException("Due date cannot be in the past");
	}
	if(task.getTitle() == null || task.getTitle().trim().isEmpty()){
	  throw new TaskValidationException("Title cannot be empty");
	}
	if(task.getPriority() == null){
	  throw new TaskValidationException("Priority is required");
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
	Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	log.info("Deleting task ID: {}", id);
	repository.deleteById(id);
  }
  
  @Override
  public List<Task> getAllTasks(Status status, Priority priority, LocalDate from, LocalDate to, String sortBy) {
	Comparator<Task> comparator = SORT_OPTIONS.getOrDefault(sortBy.toLowerCase(), Comparator.comparing(Task::getId));
	
	return repository.findAll().stream()
					 .filter(task -> status == null || task.getStatus() == status)
					 .filter(task -> priority == null || task.getPriority() == priority)
					 .filter(task -> from == null ||
									 Optional.ofNullable(task.getDueDate()).map(date -> !date.isBefore(from)).orElse(false))
					 .filter(task -> to == null ||
									 Optional.ofNullable(task.getDueDate()).map(date -> !date.isAfter(to)).orElse(false))
					 .sorted(comparator)
					 .collect(Collectors.toList());
  }
  
  @Override
  public Optional<Task> getTaskById(Long id) {
	return repository.findById(id);
  }
}