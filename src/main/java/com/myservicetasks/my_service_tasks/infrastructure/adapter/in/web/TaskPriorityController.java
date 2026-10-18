package com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web;

import com.myservicetasks.my_service_tasks.domain.model.TaskPriority;
import com.myservicetasks.my_service_tasks.domain.ports.in.TaskUseCase;
import com.myservicetasks.my_service_tasks.infrastructure.adapter.in.web.dto.CreateTaskPriorityRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-priorities")
@RequiredArgsConstructor
public class TaskPriorityController {

    private final TaskUseCase taskUseCase;

    @GetMapping
    public List<TaskPriority> getAllPriorities() {
        return taskUseCase.getPriorities();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskPriority createPriority(@Valid @RequestBody CreateTaskPriorityRequest request) {
        return taskUseCase.createPriority(request.code(), request.label(), request.color(), request.level());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePriority(@PathVariable UUID id) {
        taskUseCase.deletePriority(id);
    }
}