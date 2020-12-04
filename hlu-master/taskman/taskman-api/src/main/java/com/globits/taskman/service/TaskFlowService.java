package com.globits.taskman.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.dto.TaskFlowDto;

public interface TaskFlowService extends GenericService<TaskFlow, Long> {
	public TaskFlowDto getTaskFlowByCode(String code);
	public TaskFlowDto getTaskFlow(Long id);
	public TaskFlow getFullTaskFlowById(Long id);
	
	public TaskFlowDto saveTaskFlow(TaskFlowDto dto);
	public Boolean deleteTaskFlow(Long id);
	public Page<TaskFlowDto> getListTaskFlow(int pageSize, int pageIndex);
	public List<TaskFlowDto> getAll();
}
