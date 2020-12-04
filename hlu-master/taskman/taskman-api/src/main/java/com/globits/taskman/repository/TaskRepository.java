package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.TaskDto;
@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {
	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.flow.id=?1")
	public Page<TaskDto> getListTaskByFlowId(Long flowId,Pageable pageable);
	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null ORDER BY d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC")
	public Page<TaskDto> getListRootTaskByProjectId(Long projectId,Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1")
	public Page<TaskDto> getListTaskByProjectId(Long projectId,Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.flow.id=?1 and d.currentStep.id=?2")
	public Page<TaskDto> getListTaskByFlowAndStepId(Long flowId, Long stepId,Pageable pageable);

	
	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d ORDER BY d.createDate DESC")
	public Page<TaskDto> getListTask(Pageable pageable);
	@Query("from TaskRole where id=?1")
	public TaskRole getRole(Long id);
	@Query("from Task t left join fetch t.attachments where t.id=?1")
	public Task getFullTaskById(Long id);

	@Query("select new com.globits.taskman.dto.TaskDto(d,d.currentStep.id) from Task d where d.project.id=?1 and d.parent=null and d.currentStep.id=?2 ORDER BY d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getListRootTaskByProjectIdAndStepId(Long projectId, Long stepId, Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null and d.currentStep.id=?2 ORDER BY d.dateStart DESC, d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getProjectTasksByStepIdAndOrderByDateStart(Long projectId, Long stepId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null and d.currentStep.id=?2 ORDER BY d.dateDue DESC, d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getProjectTasksByStepIdAndOrderByDateDue(Long projectId, Long stepId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null and d.currentStep.id=?2 ORDER BY d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getProjectTasksByStepIdAndOrderByPriority(Long projectId, Long stepId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null ORDER BY d.dateStart DESC, d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getListRootTaskByProjectIdAndOrderByDateStart(Long projectId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null ORDER BY d.dateDue DESC, d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getListRootTaskByProjectIdAndOrderByDateDue(Long projectId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskDto(d) from Task d where d.project.id=?1 and d.parent=null ORDER BY d.priority.priorityOrder DESC, d.currentStep.code DESC, d.name ASC ")
	public Page<TaskDto> getListRootTaskByProjectIdAndOrderByPriority(Long projectId, Pageable pageable);
}
