package com.myservicetasks.my_service_tasks.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Integer position;
    private Instant dueDate;
    private Instant createdAt;
    private Instant updatedAt;
}