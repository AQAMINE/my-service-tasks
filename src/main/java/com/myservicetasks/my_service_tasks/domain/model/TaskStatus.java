package com.myservicetasks.my_service_tasks.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {
    private UUID id;
    private String code;
    private String label;
    private String color;
    private Integer position;
    private Boolean isSystem;
    
}
