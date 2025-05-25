package com.thewa.taskmanager.mapper;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.dto.TaskResponseDTO;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
  public static Task fromRequest(TaskRequestDTO dto) {
	return Task.builder()
			   .title(dto.getTitle() != null ? dto.getTitle() : "")
			   .description(dto.getDescription() != null ? dto.getDescription() : "")
			   .dueDate(dto.getDueDate())
			   .priority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM)
			   .status(dto.getStatus() != null ? dto.getStatus() : Status.PENDING)
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