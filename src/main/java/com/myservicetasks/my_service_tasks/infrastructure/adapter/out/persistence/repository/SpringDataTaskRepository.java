package com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.repository;

import com.myservicetasks.my_service_tasks.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query("SELECT t FROM TaskEntity t JOIN FETCH t.status JOIN FETCH t.priority WHERE t.userId = :userId ORDER BY t.status.position ASC, t.position ASC")
    List<TaskEntity> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT t FROM TaskEntity t JOIN FETCH t.status JOIN FETCH t.priority WHERE t.id = :id AND t.userId = :userId")
    Optional<TaskEntity> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}