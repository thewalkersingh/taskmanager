package com.thewa.taskmanager.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class GlobalExceptionHandlerTest {
  @Autowired
  private MockMvc mockMvc;
  
  @MockitoBean
  private TaskService taskService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Test
  @DisplayName("Should return 404 Not Found for TaskNotFoundException")
  void shouldHandleTaskNotFoundException() throws Exception {
	when(taskService.getTaskById(999L)).thenThrow(new TaskNotFoundException(999L));
	mockMvc.perform(get("/api/tasks/999"))
		   .andExpect(status().isNotFound())
		   .andExpect(content().string("Task with ID 999 not found."));
  }
  
  @Test
  @DisplayName("Should return 400 Bad Request for invalid priority value")
  void shouldReturnBadRequestForInvalidPriority() throws Exception {
	String invalidRequestBody = """
			{
			    "title": "Valid Task",
			    "priority": "ANY",
			    "dueDate": "2025-05-30"
			}
			""";
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(invalidRequestBody))
		   .andExpect(status().isBadRequest());
  }
  
  @Test
  @DisplayName("Should return 400 Bad Request for IllegalArgumentException")
  void shouldHandleIllegalArgumentException() throws Exception {
	mockMvc.perform(get("/api/tasks?priority=INVALID"))
		   .andExpect(status().isBadRequest());
  }
  
  @Test
  @DisplayName("Should return 400 Bad Request when title is missing")
  void shouldHandleTaskValidationExceptionForMissingTitle() throws Exception {
	TaskRequestDTO dto = TaskRequestDTO.builder()
									   .priority(Priority.LOW)
									   .dueDate(LocalDate.now().plusDays(5))
									   .build();
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isBadRequest())
		   .andExpect(content().string("Title cannot be empty"));
  }
}