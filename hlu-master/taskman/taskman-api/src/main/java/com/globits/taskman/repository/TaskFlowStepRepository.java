package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.dto.TaskFlowStepDto;
@Repository
public interface TaskFlowStepRepository  extends JpaRepository<TaskFlowStep, Long> {
	@Query("select new com.globits.taskman.dto.TaskFlowStepDto(d) from TaskFlowStep d")
	public Page<TaskFlowStepDto> getListTaskFlowStep(Pageable pageable);
}
