package com.thewa.taskmanager.exception;
public class TaskValidationException extends RuntimeException {
  public TaskValidationException(String message) {
	super(message);
  }
}