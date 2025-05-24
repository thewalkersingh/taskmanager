package com.thewa.taskmanager.mapper;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.dto.TaskResponseDTO;
import com.thewa.taskmanager.model.Task;

public class TaskMapper {
  
  public static Task fromRequest(TaskRequestDTO dto) {
	return Task.builder()
			   .title(dto.getTitle())
			   .description(dto.getDescription())
			   .dueDate(dto.getDueDate())
			   .priority(dto.getPriority())
			   .status(dto.getStatus())
			   .build();
  }
  
  public static TaskResponseDTO toResponse(Task task) {
	return TaskResponseDTO.builder()
						  .id(task.getId())
						  .title(task.getTitle())
						  .description(task.getDescription())
						  .dueDate(task.getDueDate())
						  .priority(task.getPriority())
						  .status(task.getStatus())
						  .build();
  }
}