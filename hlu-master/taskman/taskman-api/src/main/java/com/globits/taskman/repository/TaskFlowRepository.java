package com.globits.taskman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.dto.TaskFlowDto;
@Repository
public interface TaskFlowRepository  extends JpaRepository<TaskFlow, Long> {
	@Query("select new com.globits.taskman.dto.TaskFlowDto(d) from TaskFlow d")
	public Page<TaskFlowDto> getListTaskFlow(Pageable pageable);
	@Query("select new com.globits.taskman.dto.TaskFlowDto(d) from TaskFlow d where d.code=?1")
	public TaskFlowDto getTaskFlowByCode(String code);
	
	@Query("from TaskFlow d left join fetch d.steps s where d.id=?1")
	public TaskFlow getFullTaskFlowById(Long id);

	@Query("select new com.globits.taskman.dto.TaskFlowDto(d) from TaskFlow d ORDER BY d.name")
	public List<TaskFlowDto> getAll();
}
