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
}
