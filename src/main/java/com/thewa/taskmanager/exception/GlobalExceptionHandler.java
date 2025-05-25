package com.thewa.taskmanager.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<String> handleNotFound(TaskNotFoundException ex) {
	return ResponseEntity.status(404).body(ex.getMessage());
  }
  
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
	return ResponseEntity.badRequest().body(ex.getMessage());
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException ex) {
	String errorMessage = ex.getBindingResult().getFieldErrors().stream()
							.map(e -> e.getField() + ": " + e.getDefaultMessage())
							.collect(Collectors.joining(", "));
	return ResponseEntity.badRequest().body("Validation failed: " + errorMessage);
  }
  
  @ExceptionHandler(TaskValidationException.class)
  public ResponseEntity<String> handleTaskValidationException(TaskValidationException ex) {
	return ResponseEntity.badRequest().body(ex.getMessage());
  }
}