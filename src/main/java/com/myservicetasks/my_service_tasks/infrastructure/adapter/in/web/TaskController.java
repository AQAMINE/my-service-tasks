package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web;

import com.myservicetasks.my_service_tasks.domain.model.Task;
import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;
import com.myservicetasks.my_service_tasks.domain.ports.in.TaskUseCase;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto.CreateTaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskUseCase taskUseCase;

    @GetMapping
    public List<Task> getAllTasks(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return taskUseCase.getUserTasks(userId);
    }

    @GetMapping("/statuses")
    public List<TaskStatus> getStatuses() {
        return taskUseCase.getStatuses();
    }

    @GetMapping("/priorities")
    public List<TaskPriority> getPriorities() {
        return taskUseCase.getPriorities();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateTaskRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return taskUseCase.createTask(
                userId,
                request.title(),
                request.description(),
                request.statusId(),
                request.priorityId(),
                request.position(),
                request.dueDate()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        taskUseCase.deleteTask(id, userId);
    }
}