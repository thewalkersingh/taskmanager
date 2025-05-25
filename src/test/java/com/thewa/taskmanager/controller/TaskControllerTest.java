package com.thewa.taskmanager.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.exception.TaskNotFoundException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@DisplayName("Task Controller Tests")
class TaskControllerTest {
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @MockitoBean
  private TaskService taskService;
  
  @Test
  @DisplayName("Should create a new task successfully")
  void shouldCreateTaskSuccessfully() throws Exception {
	TaskRequestDTO dto = TaskRequestDTO.builder()
									   .title("New Task")
									   .priority(Priority.MEDIUM)
									   .dueDate(LocalDate.now().plusDays(2))
									   .build();
	
	Task task = Task.builder()
					.id(1L)
					.title(dto.getTitle())
					.dueDate(dto.getDueDate())
					.priority(dto.getPriority())
					.status(Status.PENDING)
					.build();
	
	when(taskService.createTask(any())).thenReturn(task);
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.title").value("New Task"));
  }
  
  @Test
  @DisplayName("Should retrieve all tasks")
  void shouldRetrieveAllTasks() throws Exception {
	Task task1 = new Task(1L, "Task A", "Description A", LocalDate.now(),
			Priority.HIGH, Status.PENDING);
	Task task2 = new Task(2L, "Task B", "Description B", LocalDate.now().plusDays(1),
			Priority.LOW, Status.IN_PROGRESS);
	
	when(taskService.getAllTasks(null, null, null, null, "id"))
			.thenReturn(List.of(task1, task2));
	
	mockMvc.perform(get("/api/tasks"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.length()").value(2));
  }
  
  @Test
  @DisplayName("Should retrieve a task by ID")
  void shouldRetrieveTaskById() throws Exception {
	Task task = new Task(1L, "Task A", "Description A", LocalDate.now(), Priority.HIGH, Status.PENDING);
	when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
	mockMvc.perform(get("/api/tasks/1"))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.title").value("Task A"));
  }
  
  @Test
  @DisplayName("Should return 404 Not Found for a non-existing task ID")
  void shouldReturnNotFoundForNonExistingTask() throws Exception {
	when(taskService.getTaskById(999L)).thenReturn(Optional.empty());
	mockMvc.perform(get("/api/tasks/999"))
		   .andExpect(status().isNotFound());
  }
  
  @Test
  @DisplayName("Should update a task successfully")
  void shouldUpdateTaskSuccessfully() throws Exception {
	TaskRequestDTO dto = TaskRequestDTO.builder()
									   .title("Updated Task")
									   .priority(Priority.HIGH)
									   .dueDate(LocalDate.now().plusDays(3))
									   .build();
	
	Task updatedTask = new Task(1L, dto.getTitle(),
			"Update task Desc",
			dto.getDueDate(),
			dto.getPriority(),
			Status.IN_PROGRESS);
	
	when(taskService.updateTask(any(), any())).thenReturn(updatedTask);
	
	mockMvc.perform(put("/api/tasks/1")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.title").value("Updated Task"))
		   .andExpect(jsonPath("$.priority").value("HIGH"));
  }
  
  @Test
  @DisplayName("Should delete a task successfully")
  void shouldDeleteTaskSuccessfully() throws Exception {
	doNothing().when(taskService).deleteTask(1L);
	
	mockMvc.perform(delete("/api/tasks/1"))
		   .andExpect(status().isNoContent());
  }
  
  @Test
  @DisplayName("Should return not found when deleting a non-existing task")
  void shouldReturnNotFoundWhenDeletingNonExistingTask() throws Exception {
	doThrow(new TaskNotFoundException(999L)).when(taskService).deleteTask(999L);
	mockMvc.perform(delete("/api/tasks/999"))
		   .andExpect(status().isNotFound());
  }
}