package com.thewa.taskmanager.controller;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.dto.TaskResponseDTO;
import com.thewa.taskmanager.mapper.TaskMapper;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService service;
  
  public TaskController(TaskService service) {
	this.service = service;
  }
  
  @GetMapping
  public List<TaskResponseDTO> getTasks(
		  @RequestParam(required = false) Status status,
		  @RequestParam(required = false) Priority priority,
		  @RequestParam(required = false) LocalDate from,
		  @RequestParam(required = false) LocalDate to,
		  @RequestParam(required = false, defaultValue = "id") String sortBy) {
	return service.getAllTasks(status, priority, from, to)
				  .stream()
				  .sorted(getComparator(sortBy))
				  .map(TaskMapper::toResponse)
				  .collect(Collectors.toList());
  }
  
  private Comparator<Task> getComparator(String sortBy) {
	return switch(sortBy.toLowerCase()){
	  case "status" -> Comparator.comparing(Task::getStatus);
	  case "priority" -> Comparator.comparing(Task::getPriority);
	  case "duedate" -> Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
	  default -> Comparator.comparing(Task::getId);
	};
  }
  
  @GetMapping("/{id}")
  public TaskResponseDTO getTaskById(@PathVariable Long id) {
	return TaskMapper.toResponse(service.getTaskById(id));
  }
  
  @Operation(summary = "Create a new task", description = "Provide title, priority and optional fields")
  @PostMapping
  public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO dto) {
	Task created = service.createTask(TaskMapper.fromRequest(dto));
	return TaskMapper.toResponse(created);
  }
  
  @PutMapping("/{id}")
  public TaskResponseDTO updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
	Task updated = service.updateTask(id, TaskMapper.fromRequest(dto));
	return TaskMapper.toResponse(updated);
  }
  
  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable Long id) {
	service.deleteTask(id);
  }
}
//  @GetMapping("/filtered")
//  public List<TaskResponseDTO> getFilteredTasks(@RequestParam(required = false) Status status,
//		  @RequestParam(required = false) Priority priority,
//		  @RequestParam(required = false, defaultValue = "id") String sortBy) {
//	return service.getFilteredTasks(status, priority, sortBy)
//				  .stream()
//				  .map(TaskMapper::toResponse)
//				  .collect(Collectors.toList());
//  }