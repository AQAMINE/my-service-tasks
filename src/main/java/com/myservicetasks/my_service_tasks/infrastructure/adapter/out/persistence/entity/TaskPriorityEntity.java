package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task_priorities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(length = 20)
    private String color;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "is_system", nullable = false)
    private boolean isSystem;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}