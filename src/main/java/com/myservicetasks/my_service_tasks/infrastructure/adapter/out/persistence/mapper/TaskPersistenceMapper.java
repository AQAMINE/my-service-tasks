package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.mapper;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskEntity;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskPriorityEntity;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {

    public Task toDomain(TaskEntity entity) {
        if (entity == null) return null;

        TaskStatus status = null;
        if (entity.getStatus() != null) {
            status = TaskStatus.builder()
                    .id(entity.getStatus().getId())
                    .code(entity.getStatus().getCode())
                    .label(entity.getStatus().getLabel())
                    .color(entity.getStatus().getColor())
                    .position(entity.getStatus().getPosition())
                    .isSystem(entity.getStatus().isSystem())
                    .build();
        }

        TaskPriority priority = null;
        if (entity.getPriority() != null) {
            priority = TaskPriority.builder()
                    .id(entity.getPriority().getId())
                    .code(entity.getPriority().getCode())
                    .label(entity.getPriority().getLabel())
                    .color(entity.getPriority().getColor())
                    .level(entity.getPriority().getLevel())
                    .isSystem(entity.getPriority().isSystem())
                    .build();
        }

        return Task.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(status)
                .priority(priority)
                .position(entity.getPosition())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public TaskEntity toEntity(Task domain, TaskStatusEntity statusEntity, TaskPriorityEntity priorityEntity) {
        if (domain == null) return null;

        return TaskEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .status(statusEntity)
                .priority(priorityEntity)
                .position(domain.getPosition())
                .dueDate(domain.getDueDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}