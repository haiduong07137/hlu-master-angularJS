package com.globits.taskman.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.dto.TaskFlowStepDto;

public interface TaskFlowStepService extends GenericService<TaskFlowStep, Long> {
	public TaskFlowStepDto getTaskFlowStep(Long id);
	public TaskFlowStepDto saveTaskFlowStep(TaskFlowStepDto dto);
	public Boolean deleteTaskFlowStep(Long id);
	public Page<TaskFlowStepDto> getListTaskFlowStep(int pageSize, int pageIndex);
}
