package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web;

import com.myservicetasks.my_service_tasks.domain.model.TaskStatus;
import com.myservicetasks.my_service_tasks.domain.ports.in.TaskUseCase;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto.CreateTaskStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-statuses")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskUseCase taskUseCase;

    @GetMapping
    public List<TaskStatus> getAllStatuses() {
        return taskUseCase.getStatuses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createStatus(@Valid @RequestBody CreateTaskStatusRequest request) {
        return taskUseCase.createStatus(request.code(), request.label(), request.color(), request.position());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStatus(@PathVariable UUID id) {
        taskUseCase.deleteStatus(id);
    }
}