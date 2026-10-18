package com.myservicetasks.my_service_tasks.domain.ports.out;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task, UUID statusId, UUID priorityId);
    List<Task> findByUserId(UUID userId);
    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
    Task update(UUID id, UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate, boolean updateDueDate);
    void delete(UUID id);

    // Statuses
    List<TaskStatus> findAllStatuses();
    Optional<TaskStatus> findStatusById(UUID id);
    TaskStatus saveStatus(TaskStatus status);
    void deleteStatus(UUID id);

    // Priorities
    List<TaskPriority> findAllPriorities();
    Optional<TaskPriority> findPriorityById(UUID id);
    TaskPriority savePriority(TaskPriority priority);
    void deletePriority(UUID id);
}