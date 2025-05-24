package com.thewa.taskmanager.service;
import com.thewa.taskmanager.exception.TaskNotFoundException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.repositroy.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
  private TaskRepository repo;
  private TaskServiceImpl service;
  
  @BeforeEach
  void setup() {
	repo = mock(TaskRepository.class);
	service = new TaskServiceImpl(repo);
  }
  
  @Test
  void testCreateTask_success() {
	Task input = new Task(null, "Test", "Desc", null, Priority.MEDIUM, null);
	Task saved = new Task(1L, "Test", "Desc", null, Priority.MEDIUM, Status.PENDING);
	when(repo.save(any(Task.class))).thenReturn(saved);
	Task result = service.createTask(input);
	assertEquals("Test", result.getTitle());
	assertEquals(Status.PENDING, result.getStatus());
  }
  
  @Test
  void testUpdateTask_invalidId() {
	when(repo.findById(1L)).thenReturn(Optional.empty());
	Task input = new Task(null, "Updated", null, null, null, null);
	assertThrows(TaskNotFoundException.class, () -> service.updateTask(1L, input));
  }
  
  @Test
  void testDeleteTask_validId() {
	when(repo.findById(1L)).thenReturn(Optional.of(new Task()));
	doNothing().when(repo).deleteById(1L);
	assertDoesNotThrow(() -> service.deleteTask(1L));
  }
  
  @Test
  void testGetAllTasks_filterByPriority() {
	Task t1 = new Task(1L, "A", "", null, Priority.HIGH, Status.PENDING);
	Task t2 = new Task(2L, "B", "", null, Priority.LOW, Status.PENDING);
	when(repo.findAll()).thenReturn(List.of(t1, t2));
	List<Task> result = service.getAllTasks(null, Priority.HIGH, null, null);
	assertEquals(1, result.size());
	assertEquals(Priority.HIGH, result.get(0).getPriority());
  }
  
  @Test
  void testCreateTaskWithNullTitleThrows() {
	Task task = new Task(null, null, null, null, Priority.LOW, null);
	assertThrows(IllegalArgumentException.class, () -> service.createTask(task));
  }
  
  @Test
  void testUpdateNonExistentTaskThrows() {
	when(repo.findById(42L)).thenReturn(Optional.empty());
	assertThrows(TaskNotFoundException.class, () -> service.updateTask(42L, new Task()));
  }
  
  // Cli tests
  @Test
  void testGetFilteredTasks_sortByPriority() {
	Task t1 = new Task(1L, "Low", "", null, Priority.LOW, Status.PENDING);
	Task t2 = new Task(2L, "High", "", null, Priority.HIGH, Status.PENDING);
	Task t3 = new Task(3L, "Medium", "", null, Priority.MEDIUM, Status.PENDING);
	
	when(repo.findAll()).thenReturn(List.of(t1, t2, t3));
	
	List<Task> result = service.getFilteredTasks(null, null, "priority");
	assertEquals(3, result.size());
	assertEquals("Low", result.get(0).getTitle()); // Because enum order: LOW < MEDIUM < HIGH
  }
  
  @Test
  void testGetFilteredTasks_sortByDueDate() {
	Task t1 = new Task(1L, "A", "", LocalDate.now().plusDays(3), Priority.LOW, Status.PENDING);
	Task t2 = new Task(2L, "B", "", LocalDate.now().plusDays(1), Priority.LOW, Status.PENDING);
	Task t3 = new Task(3L, "C", "", LocalDate.now().plusDays(2), Priority.LOW, Status.PENDING);
	
	when(repo.findAll()).thenReturn(List.of(t1, t2, t3));
	
	List<Task> result = service.getFilteredTasks(null, null, "dueDate");
	assertEquals("B", result.get(0).getTitle());
  }
}