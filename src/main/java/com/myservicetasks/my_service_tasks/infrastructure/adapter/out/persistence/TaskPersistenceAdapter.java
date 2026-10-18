package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;
import com.myservicetasks.my_service_tasks.domain.ports.out.TaskRepositoryPort;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskEntity;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskPriorityEntity;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskStatusEntity;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.mapper.TaskPersistenceMapper;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository.SpringDataTaskPriorityRepository;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository.SpringDataTaskRepository;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository.SpringDataTaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository taskRepository;
    private final SpringDataTaskStatusRepository statusRepository;
    private final SpringDataTaskPriorityRepository priorityRepository;
    private final TaskPersistenceMapper mapper;

    @Override
    public Task save(Task task, UUID statusId, UUID priorityId) {
        TaskStatusEntity status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Statut introuvable"));
        TaskPriorityEntity priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new IllegalArgumentException("Priorité introuvable"));

        TaskEntity entity = mapper.toEntity(task, status, priority);
        TaskEntity saved = taskRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Task> findByUserId(UUID userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findByIdAndUserId(UUID id, UUID userId) {
        return taskRepository.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public Task update(UUID id, UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate, boolean updateDueDate) {
        TaskEntity entity = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée"));

        if (title != null) {
            entity.setTitle(title);
        }
        if (description != null) {
            entity.setDescription(description);
        }
        if (position != null) {
            entity.setPosition(position);
        }
        if (updateDueDate) {
            entity.setDueDate(dueDate);
        }
        if (statusId != null) {
            TaskStatusEntity status = statusRepository.findById(statusId)
                    .orElseThrow(() -> new IllegalArgumentException("Statut introuvable"));
            entity.setStatus(status);
        }
        if (priorityId != null) {
            TaskPriorityEntity priority = priorityRepository.findById(priorityId)
                    .orElseThrow(() -> new IllegalArgumentException("Priorité introuvable"));
            entity.setPriority(priority);
        }

        return mapper.toDomain(taskRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskStatus> findAllStatuses() {
        return statusRepository.findAllByOrderByPositionAsc().stream()
                .map(s -> TaskStatus.builder()
                        .id(s.getId())
                        .code(s.getCode())
                        .label(s.getLabel())
                        .color(s.getColor())
                        .position(s.getPosition())
                        .isSystem(s.isSystem())
                        .build())
                .toList();
    }

    @Override
    public Optional<TaskStatus> findStatusById(UUID id) {
        return statusRepository.findById(id)
                .map(s -> TaskStatus.builder()
                        .id(s.getId())
                        .code(s.getCode())
                        .label(s.getLabel())
                        .color(s.getColor())
                        .position(s.getPosition())
                        .isSystem(s.isSystem())
                        .build());
    }

    @Override
    public TaskStatus saveStatus(TaskStatus status) {
        TaskStatusEntity entity = TaskStatusEntity.builder()
                .id(status.getId())
                .code(status.getCode())
                .label(status.getLabel())
                .color(status.getColor())
                .position(status.getPosition())
                .isSystem(status.getIsSystem() != null ? status.getIsSystem() : false)
                .build();
        TaskStatusEntity saved = statusRepository.save(entity);
        return TaskStatus.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .label(saved.getLabel())
                .color(saved.getColor())
                .position(saved.getPosition())
                .isSystem(saved.isSystem())
                .build();
    }

    @Override
    public void deleteStatus(UUID id) {
        statusRepository.deleteById(id);
    }

    @Override
    public List<TaskPriority> findAllPriorities() {
        return priorityRepository.findAllByOrderByLevelAsc().stream()
                .map(p -> TaskPriority.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .label(p.getLabel())
                        .color(p.getColor())
                        .level(p.getLevel())
                        .isSystem(p.isSystem())
                        .build())
                .toList();
    }

    @Override
    public Optional<TaskPriority> findPriorityById(UUID id) {
        return priorityRepository.findById(id)
                .map(p -> TaskPriority.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .label(p.getLabel())
                        .color(p.getColor())
                        .level(p.getLevel())
                        .isSystem(p.isSystem())
                        .build());
    }

    @Override
    public TaskPriority savePriority(TaskPriority priority) {
        TaskPriorityEntity entity = TaskPriorityEntity.builder()
                .id(priority.getId())
                .code(priority.getCode())
                .label(priority.getLabel())
                .color(priority.getColor())
                .level(priority.getLevel())
                .isSystem(priority.getIsSystem() != null ? priority.getIsSystem() : false)
                .build();
        TaskPriorityEntity saved = priorityRepository.save(entity);
        return TaskPriority.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .label(saved.getLabel())
                .color(saved.getColor())
                .level(saved.getLevel())
                .isSystem(saved.isSystem())
                .build();
    }

    @Override
    public void deletePriority(UUID id) {
        priorityRepository.deleteById(id);
    }
}