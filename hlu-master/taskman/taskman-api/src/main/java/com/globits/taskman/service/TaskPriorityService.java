package com.globits.taskman.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskPriority;
import com.globits.taskman.dto.TaskPriorityDto;

public interface TaskPriorityService extends GenericService<TaskPriority, Long> {
	public TaskPriorityDto getTaskPriority(Long id);
	public TaskPriorityDto saveTaskPriority(TaskPriorityDto dto);
	public Boolean deleteTaskPriority(Long id);
	public Page<TaskPriorityDto> getListTaskPriority(int pageSize, int pageIndex);
}
