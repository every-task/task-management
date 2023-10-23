package com.playdata.domain.task.repository;

import com.playdata.domain.task.entity.TaskInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskInformationRepository extends JpaRepository<TaskInformation, UUID> {
}
