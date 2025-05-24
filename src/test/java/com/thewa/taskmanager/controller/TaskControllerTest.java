package com.thewa.taskmanager.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewa.taskmanager.dto.TaskRequestDTO;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private TaskService taskService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Test
  void testCreateTask_success() throws Exception {
	TaskRequestDTO dto = new TaskRequestDTO();
	dto.setTitle("New Task");
	dto.setPriority(Priority.MEDIUM);
	dto.setDueDate(LocalDate.now().plusDays(2));
	
	Task task = new Task(1L, "New Task", "", dto.getDueDate(), Priority.MEDIUM, Status.PENDING);
	when(taskService.createTask(any())).thenReturn(task);
	
	mockMvc.perform(post("/api/tasks")
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(dto)))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.title").value("New Task"));
  }
}