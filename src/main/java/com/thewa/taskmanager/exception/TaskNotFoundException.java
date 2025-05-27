package com.thewa.taskmanager.exception;
public class TaskNotFoundException extends RuntimeException {
   public TaskNotFoundException(Long id) {
	  super("Task with ID " + id + " not found.");
   }
}