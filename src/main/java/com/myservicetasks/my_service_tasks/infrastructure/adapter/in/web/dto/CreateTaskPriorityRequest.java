package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskPriorityRequest(
    @NotBlank(message = "Le code est obligatoire") @Size(max = 50) String code,
    @NotBlank(message = "Le libellé est obligatoire") @Size(max = 100) String label,
    @Size(max = 20) String color,
    Integer level
) {}