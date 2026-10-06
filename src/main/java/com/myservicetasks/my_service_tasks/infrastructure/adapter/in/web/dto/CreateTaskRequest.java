package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CreateTaskRequest(
    @NotBlank(message = "Le titre est obligatoire") String title,
    String description,
    @NotNull(message = "L'ID du statut est obligatoire") UUID statusId,
    @NotNull(message = "L'ID de la priorité est obligatoire") UUID priorityId,
    Integer position,
    Instant dueDate
) {}