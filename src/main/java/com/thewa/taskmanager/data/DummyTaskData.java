package com.thewa.taskmanager.data;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DummyTaskData implements CommandLineRunner {
  private final TaskService taskService;
  
  public DummyTaskData(TaskService taskService) {
	this.taskService = taskService;
  }
  
  public void run(String... args) {
	taskService.createTask(new Task(null, "Read Spring Docs", "Study About spring boot",
			LocalDate.now().plusDays(3), Priority.HIGH,
			Status.PENDING));
	taskService.createTask(new Task(null, "Watch Playlist", "Finish concepts",
			LocalDate.now().plusDays(5),
			Priority.MEDIUM, Status.IN_PROGRESS));
	
	taskService.createTask(new Task(null, "Walking", "Walk for 45 minutes",
			LocalDate.now().plusDays(1), Priority.HIGH,
			Status.PENDING));
	taskService.createTask(new Task(null, "Buy Items", "Milk, eggs, coffee",
			LocalDate.now().plusDays(2), Priority.LOW,
			Status.COMPLETED));
	taskService.createTask(new Task(null, "Fix whiteboard", "Fix the whiteboard",
			LocalDate.now().plusDays(4), Priority.MEDIUM,
			Status.IN_PROGRESS));
	taskService.createTask(new Task(null, "Create Task Project", "Create REST API project",
			LocalDate.now().plusDays(7), Priority.HIGH,
			Status.IN_PROGRESS));
	taskService.createTask(new Task(null, "Update ReviveHub", "Update the ReviveHub API",
			LocalDate.now().plusDays(4), Priority.LOW,
			Status.COMPLETED));
	taskService.createTask(new Task(null, "Complete TaskManager", "Complete the project",
			LocalDate.now().plusDays(2), Priority.HIGH,
			Status.PENDING));
	
	System.out.println("Dummy task data loaded successfully.");
  }
}