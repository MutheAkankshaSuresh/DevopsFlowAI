package com.devopsflowai.repository;

import com.devopsflowai.entity.TaskItem;
import com.devopsflowai.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    long countByStatus(TaskStatus status);
    List<TaskItem> findByDeadlineBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
