package com.thewa.taskmanager.dto;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
   @NotBlank(message = "Title cannot be empty")
   private String title;
   private String description;
   
   @FutureOrPresent(message = "Due date cannot be in the past")
   private LocalDate dueDate;
   
   @NotNull(message = "Priority is required")
   private Priority priority;
   private Status status;
}