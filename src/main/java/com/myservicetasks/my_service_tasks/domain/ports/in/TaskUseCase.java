package com.myservicetasks.my_service_tasks.domain.ports.in;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskUseCase {
    List<Task> getUserTasks(UUID userId);
    Task createTask(UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, java.time.Instant dueDate);
    void deleteTask(UUID id, UUID userId);
    List<TaskStatus> getStatuses();
    List<TaskPriority> getPriorities();
}
