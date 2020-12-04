package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskPriority;
import com.globits.taskman.dto.TaskPriorityDto;
@Repository
public interface TaskPriorityRepository  extends JpaRepository<TaskPriority, Long> {
	@Query("select new com.globits.taskman.dto.TaskPriorityDto(d) from TaskPriority d")
	public Page<TaskPriorityDto> getListTaskPriority(Pageable pageable);
}
