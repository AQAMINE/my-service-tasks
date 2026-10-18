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
    public Task updateTask(UUID id, UUID userId, String title, String description, UUID statusId, UUID priorityId, Integer position, Instant dueDate, boolean updateDueDate) {
        taskRepositoryPort.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée"));

        if (title != null && title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }

        return taskRepositoryPort.update(
                id,
                userId,
                title != null ? title.trim() : null,
                description,
                statusId,
                priorityId,
                position,
                dueDate,
                updateDueDate
        );
    }

    @Override
    public void deleteTask(UUID id, UUID userId) {
        Task task = taskRepositoryPort.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Tâche non trouvée"));
        taskRepositoryPort.delete(task.getId());
    }

    // --- STATUSES ---

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatus> getStatuses() {
        return taskRepositoryPort.findAllStatuses();
    }

    @Override
    public TaskStatus createStatus(String code, String label, String color, Integer position) {
        TaskStatus status = TaskStatus.builder()
                .code(code.toUpperCase())
                .label(label)
                .color(color != null ? color : "#6C757D")
                .position(position != null ? position : 0)
                .isSystem(false)
                .build();
        return taskRepositoryPort.saveStatus(status);
    }

    @Override
    public void deleteStatus(UUID id) {
        TaskStatus status = taskRepositoryPort.findStatusById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statut introuvable"));
        if (Boolean.TRUE.equals(status.getIsSystem())) {
            throw new IllegalStateException("Impossible de supprimer un statut système");
        }
        taskRepositoryPort.deleteStatus(id);
    }

    // --- PRIORITIES ---

    @Override
    @Transactional(readOnly = true)
    public List<TaskPriority> getPriorities() {
        return taskRepositoryPort.findAllPriorities();
    }

    @Override
    public TaskPriority createPriority(String code, String label, String color, Integer level) {
        TaskPriority priority = TaskPriority.builder()
                .code(code.toUpperCase())
                .label(label)
                .color(color != null ? color : "#6C757D")
                .level(level != null ? level : 0)
                .isSystem(false)
                .build();
        return taskRepositoryPort.savePriority(priority);
    }

    @Override
    public void deletePriority(UUID id) {
        TaskPriority priority = taskRepositoryPort.findPriorityById(id)
                .orElseThrow(() -> new IllegalArgumentException("Priorité introuvable"));
        if (Boolean.TRUE.equals(priority.getIsSystem())) {
            throw new IllegalStateException("Impossible de supprimer une priorité système");
        }
        taskRepositoryPort.deletePriority(id);
    }
}