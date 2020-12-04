package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.dto.TaskStepDto;
@Repository
public interface TaskStepRepository  extends JpaRepository<TaskStep, Long> {
	@Query("select new com.globits.taskman.dto.TaskStepDto(d) from TaskStep d")
	public Page<TaskStepDto> getListTaskStep(Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.TaskStepDto(d) from TaskStep d where d.code=?1")
	public TaskStepDto getTaskStepByCode(String stepCode);
	
	@Query("from TaskStep d where d.code=?1")
	public TaskStep getTaskStepEntityByCode(String stepCode);
}
