package com.myservicetasks.my_service_tasks.domain.ports.in;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TaskUseCase {
    // Tasks
    List<Task> getUserTasks(UUID userId);
    Task createTask(UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate);
    Task updateTask(UUID id, UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate, boolean updateDueDate);
    void deleteTask(UUID id, UUID userId);

    // Statuses
    List<TaskStatus> getStatuses();
    TaskStatus createStatus(String code, String label, String color, Integer position);
    void deleteStatus(UUID id);

    // Priorities
    List<TaskPriority> getPriorities();
    TaskPriority createPriority(String code, String label, String color, Integer level);
    void deletePriority(UUID id);
}