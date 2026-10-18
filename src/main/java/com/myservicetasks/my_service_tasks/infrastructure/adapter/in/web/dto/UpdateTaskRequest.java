package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record UpdateTaskRequest(
        @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères") String title,
        String description,
        UUID statusId,
        UUID priorityId,
        Integer position,
        Instant dueDate,
        Boolean updateDueDate
) {}
