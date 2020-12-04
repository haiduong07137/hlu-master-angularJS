package com.globits.taskman.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.Task;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;

public interface TaskService extends GenericService<Task, Long> {
	public TaskDto getTask(Long id);
	public TaskDto saveTask(TaskDto dto);
	public Boolean deleteTask(Long id);
	public Page<TaskDto> getListTask(int pageSize, int pageIndex);
	public Page<TaskDto> getListTaskByFlow(Long flowId, int pageSize, int pageIndex);
	public Page<TaskDto> getListPersonalTask(int pageSize, int pageIndex);
	Page<TaskDto> getListPersonalTask(Long staffId, List<Long> roleIds, Long flowId, int pageSize, int pageIndex);
	Page<TaskDto> getListDepartmentTask(List<Long> departmentIds, List<Long> roleIds, Long flowId, Integer currentState,
			int pageSize, int pageIndex);
	Page<TaskDto> getAllTask(Long staffId, Long departmentId, Long flowId, Integer currentState, int pageSize,
			int pageIndex);
	Page<TaskDto> getAllDepartmentAndPersonalTask(Long staffId, Long flowId,Long stepId, Integer currentParticipateState, int pageSize,int pageIndex);
	Page<TaskDto> getAllDepartmentAndPersonalTask(Long flowId,Long stepId, Integer currentParticipateState, int pageSize,int pageIndex);
	Page<TaskDto> getListTaskByFlowAndStepId(Long flowId, Long stepId, int pageSize, int pageIndex);
	Boolean addComment(TaskCommentDto comment);
	
	Page<TaskDto> getAllTaskByProject(Long projectId, int pageSize, int pageIndex);
	Page<TaskDto> getListRootTaskByProjectId(Long projectId, int pageSize, int pageIndex);
	public Page<TaskDto> getListRootTaskByProjectIdAndStepId(Long projectId, Long stepId, int pageSize, int pageIndex);
	public Page<TaskDto> getProjectTasksByStepIdAndOrderByType(Long projectId, Long stepId, int orderByType,
			int pageSize, int pageIndex);
	public Page<TaskDto> getListRootTaskByProjectIdAndOrderByType(Long projectId, int orderByType, int pageSize,
			int pageIndex);
	Page<TaskDto> getListRootTaskByProjectIdAndStepId(Long projectId, Long stepId, Long priorityId, Long userId,
			int pageSize, int pageIndex);

	Page<TaskDto> getMyRootTaskByProjectIdAndStepId(Long projectId, Long stepId, Long priorityId, int pageSize,
			int pageIndex);
	public Page<TaskDto> getListTaskBy(long flowId, long stepId, int pageIndex, int pageSize);
	public TaskDto saveDaiLyWorks(TaskDto dto);
	
}
