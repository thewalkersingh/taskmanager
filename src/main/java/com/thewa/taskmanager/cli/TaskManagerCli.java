package com.thewa.taskmanager.cli;
import com.thewa.taskmanager.exception.InvalidOptionException;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import com.thewa.taskmanager.model.Task;
import com.thewa.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
@Order(2)
@Profile("cli")
public class TaskManagerCli implements CommandLineRunner {
  private final TaskService taskService;
  private static final Logger log = LoggerFactory.getLogger(TaskManagerCli.class);
  public TaskManagerCli(final TaskService taskService) {
	this.taskService = taskService;
  }
  
  @Override
  public void run(final String... args) throws Exception {
	Scanner sc = new Scanner(System.in);
	System.out.println("Welcome to Task CLI");
	while(true){
	  System.out.println("\nChoose an option:");
	  System.out.println("1. List all tasks");
	  System.out.println("2. Create a task");
	  System.out.println("3. List tasks sorted by priority");
	  System.out.println("4. List tasks sorted by due date");
	  System.out.println("5. Exit");
	  System.out.print("Enter your choice: ");
	  int choice = sc.nextInt();
	  sc.nextLine();
	  log.info("User selected menu option: {}", choice);
	  switch(choice){
		case 1:
		  listTasks();
		  break;
		case 2:
		  createTask(sc);
		  break;
		case 3:
		  listSortedTasks("priority");
		  break;
		case 4:
		  listSortedTasks("dueDate");
		  break;
		case 5:
		  System.out.println("Exiting.. Program");
		  return;
		
		default:
		  System.out.println("Invalid option");
		  throw new InvalidOptionException("You have selected Invalid Option");
	  }
	}
  }
  
  private void listTasks() {
	List<Task> tasks = taskService.getAllTasks(null, null, null, null);
	tasks.forEach(
			task -> System.out.println(
					"ID: " + task.getId() + " Title: " + task.getTitle() + " [" + task.getStatus() +
					"]" + " DueDate: " + task.getDueDate() + " Priority: " + task.getPriority()));
  }
  
  private void createTask(Scanner scanner) {
	System.out.print("Enter title: ");
	String title = scanner.nextLine();
	
	System.out.print("Enter description: ");
	String desc = scanner.nextLine();
	
	System.out.print("Enter due date (yyyy-MM-dd): ");
	String dateStr = scanner.nextLine();
	LocalDate dueDate;
	try{
	  dueDate = LocalDate.parse(dateStr);
	} catch(DateTimeParseException e){
	  System.out.println("Invalid date format! Use yyyy-MM-dd.");
	  return;
	}
	
	System.out.print("Enter priority (LOW, MEDIUM, HIGH): ");
	Priority priority;
	try{
	  priority = Priority.valueOf(scanner.nextLine().toUpperCase());
	} catch(IllegalArgumentException e){
	  System.out.println("Invalid priority! Must be LOW, MEDIUM, or HIGH.");
	  return;
	}
	
	Task task = new Task(null, title, desc, dueDate, priority, Status.PENDING);
	taskService.createTask(task);
	log.info("Creating task from CLI: {}", title);
	System.out.println("Task created successfully.");
  }
  
  private void listSortedTasks(String sortBy) {
	List<Task> tasks = taskService.getFilteredTasks(null, null, sortBy);
	tasks.forEach(task ->
			System.out.println("ID: " + task.getId() +
							   " | Title: " + task.getTitle() +
							   " | Priority: " + task.getPriority() +
							   " | Due: " + task.getDueDate() +
							   " | Status: " + task.getStatus()));
  }
}