package com.globits.taskman.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.dto.TaskStepDto;


public interface TaskStepService extends GenericService<TaskStep, Long> {
	public TaskStepDto getTaskStep(Long id);
	public TaskStepDto saveTaskStep(TaskStepDto dto);
	public Boolean deleteTaskStep(Long id);
	public Page<TaskStepDto> getListTaskStep(int pageSize, int pageIndex);
	public TaskStepDto getTaskStepByCode(String code);
	public TaskStep getTaskStepEntityByCode(String stepCode);;
}
