package com.thewa.taskmanager.dto;
import com.thewa.taskmanager.model.Priority;
import com.thewa.taskmanager.model.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
   private Long id;
   private String title;
   private String description;
   private LocalDate dueDate;
   private Priority priority;
   private Status status;
}