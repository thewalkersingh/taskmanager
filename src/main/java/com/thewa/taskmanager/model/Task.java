package com.thewa.taskmanager.model;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Task {
   private Long id;
   private String title;
   private String description;
   private LocalDate dueDate;
   private Priority priority;
   private Status status;
}