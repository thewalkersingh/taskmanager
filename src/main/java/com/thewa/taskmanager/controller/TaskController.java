package com.thewa.taskmanager.controller;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.dto.TaskResponseDTO;
import com.thewa.taskmanager.exception.TaskValidationException;
import com.thewa.taskmanager.mapper.TaskMapper;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
   private final TaskService service;
   
   public TaskController(TaskService service) {
	  this.service = service;
   }
   
   @Operation(summary = "Get all existing task list based on any options")
   @GetMapping
   public List<TaskResponseDTO> getTasks(
		   @RequestParam(required = false) Status status,
		   @RequestParam(required = false) Priority priority,
		   @RequestParam(required = false) LocalDate from,
		   @RequestParam(required = false) LocalDate to,
		   @RequestParam(required = false, defaultValue = "id") String sortBy) {
	  
	  return service.getAllTasks(status, priority, from, to, sortBy)
					.stream()
					.map(TaskMapper::toResponse)
					.collect(Collectors.toList());
   }
   
   @Operation(summary = "Get the task based on id")
   @GetMapping("/{id}")
   public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
	  return service.getTaskById(id)
					.map(task -> ResponseEntity.ok(TaskMapper.toResponse(task)))
					.orElseGet(() -> ResponseEntity.notFound().build());
   }
   
   @Operation(summary = "Create a new task")
   @PostMapping
   public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto) {
	  Task created = service.createTask(TaskMapper.fromRequest(dto));
	  if(created == null){
		 throw new TaskValidationException("Title cannot be empty");
	  }
	  return ResponseEntity.ok(TaskMapper.toResponse(created));
   }
   
   @Operation(summary = "Update the existing task")
   @PutMapping("/{id}")
   public TaskResponseDTO updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
	  Task updated = service.updateTask(id, TaskMapper.fromRequest(dto));
	  return TaskMapper.toResponse(updated);
   }
   
   @Operation(summary = "Delete the existing task by Id")
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
	  service.deleteTask(id);
	  return ResponseEntity.noContent().build();
   }
}