package com.thewa.taskmanager.data;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(1)
public class DummyTaskData implements CommandLineRunner {
  private final TaskService taskService;
  
  public DummyTaskData(TaskService taskService) {
	this.taskService = taskService;
  }
  
  public void run(String... args) {
	taskService.createTask(new Task(null, "Read Spring Docs", "Study DI, AOP",
			LocalDate.now().plusDays(3), Priority.HIGH,
			Status.PENDING));
	taskService.createTask(new Task(null, "Watch Java Playlist", "Finish OOP concepts",
			LocalDate.now().plusDays(5),
			Priority.MEDIUM, Status.IN_PROGRESS));
	
	taskService.createTask(new Task(null, "Workout", "Do 100 pushups",
			LocalDate.now().plusDays(1), Priority.HIGH,
			Status.PENDING));
	taskService.createTask(new Task(null, "Buy Groceries", "Milk, eggs, bread",
			LocalDate.now().plusDays(2), Priority.LOW,
			Status.COMPLETED));
	taskService.createTask(new Task(null, "Fix Bugs", "Debug the feature branch",
			LocalDate.now().plusDays(4), Priority.MEDIUM,
			Status.IN_PROGRESS));
	
	System.out.println("Dummy task data loaded successfully.");
  }
}