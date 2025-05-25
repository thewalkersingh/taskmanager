package com.thewa.taskmanager.service;
import com.thewa.taskmanager.exception.TaskNotFoundException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
  @InjectMocks
  private TaskServiceImpl service;
  
  @Mock
  private TaskRepository repo;
  
  @Test
  @DisplayName("Should create a task with default status PENDING when status is null")
  void testCreateTask_success() {
	Task input = new Task(null, "Test", "Desc", null, Priority.MEDIUM, null);
	Task saved = new Task(1L, "Test", "Desc", null, Priority.MEDIUM, Status.PENDING);
	when(repo.save(any(Task.class))).thenReturn(saved);
	Task result = service.createTask(input);
	assertThat(result.getTitle()).isEqualTo("Test");
	assertThat(result.getStatus()).isEqualTo(Status.PENDING);
  }
  
  @Test
  @DisplayName("Should Not update if ID not found")
  void testUpdateTask_invalidId() {
	when(repo.findById(1L)).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.updateTask(1L,
			new Task(null, "Test", "Desc", null, Priority.MEDIUM, null)))
			.isInstanceOf(TaskNotFoundException.class)
			.hasMessageContaining("Task with ID 1 not found");
  }
  
  @Test
  @DisplayName("Should delete an existing task successfully")
  void testDeleteTask_validId() {
	when(repo.findById(1L)).thenReturn(Optional.of(
			new Task(1L, "Test", "Desc", null, Priority.MEDIUM, Status.PENDING)));
	doNothing().when(repo).deleteById(1L);
	assertDoesNotThrow(() -> service.deleteTask(1L));
	verify(repo, times(1)).deleteById(1L);
  }
  
  @Test
  @DisplayName("Should Display all tasks based on priority")
  void testGetAllTasks_filterByPriority() {
	Task t1 = new Task(1L, "A", "", null, Priority.HIGH, Status.PENDING);
	Task t2 = new Task(2L, "B", "", null, Priority.LOW, Status.PENDING);
	when(repo.findAll()).thenReturn(List.of(t1, t2));
	List<Task> result = service.getAllTasks(null, Priority.HIGH, null, null, "id");
	assertThat(result).extracting(Task::getPriority).containsOnly(Priority.HIGH);
  }
  
  @Test
  @DisplayName("Should handle concurrent task updates correctly")
  void testConcurrencyHandling() throws InterruptedException {
	ExecutorService executor = Executors.newFixedThreadPool(2);
	Task initialTask = new Task(1L, "Test", "Desc", null, Priority.MEDIUM, Status.PENDING);
	when(repo.findById(1L)).thenReturn(Optional.of(initialTask));
	Runnable taskUpdater1 = () -> service.updateTask(1L,
			new Task(1L, "Updated Task 1", "", null, Priority.HIGH, Status.IN_PROGRESS));
	Runnable taskUpdater2 = () -> service.updateTask(1L,
			new Task(1L, "Updated Task 2", "", null, Priority.LOW, Status.COMPLETED));
	executor.execute(taskUpdater1);
	executor.execute(taskUpdater2);
	executor.shutdown();
	executor.awaitTermination(2, TimeUnit.SECONDS);
	verify(repo, atLeast(2)).findById(1L);
  }
}