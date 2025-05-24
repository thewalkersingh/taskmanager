package com.thewa.taskmanager.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewa.taskmanager.controller.TaskController;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class GlobalExceptionHandlerTest {
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private TaskService taskService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Test
  void testValidationFailsForEmptyTitle() throws Exception {
	TaskRequestDTO dto = new TaskRequestDTO();
	dto.setTitle("  ");
	dto.setPriority(Priority.HIGH);
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isBadRequest())
		   .andExpect(content().string(org.hamcrest.Matchers.containsString("Title is required")));
  }
  
  @Test
  void testValidationFailsForPastDueDate() throws Exception {
	TaskRequestDTO dto = new TaskRequestDTO();
	dto.setTitle("My Task");
	dto.setPriority(Priority.LOW);
	dto.setDueDate(LocalDate.now().minusDays(1));
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isBadRequest())
		   .andExpect(content().string(org.hamcrest.Matchers.containsString("Due date cannot be in the past")));
  }
  
  @Test
  void testTaskNotFoundReturns404() throws Exception {
	when(taskService.getTaskById(999L)).thenThrow(new com.thewa.taskmanager.exception.TaskNotFoundException(999L));
	
	mockMvc.perform(get("/api/tasks/999"))
		   .andExpect(status().isNotFound())
		   .andExpect(content().string(org.hamcrest.Matchers.containsString("Task with ID 999 not found")));
  }
}