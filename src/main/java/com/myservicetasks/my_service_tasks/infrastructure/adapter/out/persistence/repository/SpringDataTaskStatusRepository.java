package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository;

import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTaskStatusRepository extends JpaRepository<TaskStatusEntity, UUID> {
    List<TaskStatusEntity> findAllByOrderByPositionAsc();
    Optional<TaskStatusEntity> findByCode(String code);
}