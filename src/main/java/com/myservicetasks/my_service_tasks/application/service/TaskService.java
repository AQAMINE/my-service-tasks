package com.myservicetasks.my_service_tasks.application.service;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;
import com.myservicetasks.my_service_tasks.domain.ports.in.TaskUseCase;
import com.myservicetasks.my_service_tasks.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<Task> getUserTasks(UUID userId) {
        return taskRepositoryPort.findByUserId(userId);
    }

    @Override
    public Task createTask(UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate) {
        Task task = Task.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .position(position != null ? position : 0)
                .dueDate(dueDate)
                .build();

        return taskRepositoryPort.save(task, statusId, priorityId);
    }

    @Override
    public void deleteTask(UUID id, UUID userId) {
        Task task = taskRepositoryPort.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée"));
        taskRepositoryPort.delete(task.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> getStatuses() {
        return taskRepositoryPort.findAllStatuses();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskPriority> getPriorities() {
        return taskRepositoryPort.findAllPriorities();
    }
}
