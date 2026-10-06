package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository;

import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskPriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTaskPriorityRepository extends JpaRepository<TaskPriorityEntity, UUID> {
    List<TaskPriorityEntity> findAllByOrderByLevelAsc();
    Optional<TaskPriorityEntity> findByCode(String code);
}