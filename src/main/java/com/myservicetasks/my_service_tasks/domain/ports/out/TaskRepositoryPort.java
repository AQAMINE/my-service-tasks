package com.myservicetasks.my_service_tasks.domain.ports.out;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task, UUID statusId, UUID priorityId);
    List<Task> findByUserId(UUID userId);
    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
    void delete(UUID id);
    
    List<TaskStatus> findAllStatuses();
    Optional<TaskStatus> findStatusById(UUID id);
    
    List<TaskPriority> findAllPriorities();
    Optional<TaskPriority> findPriorityById(UUID id);
}
