package com.globits.letter.service;

import org.springframework.data.domain.Page;

import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;

public interface WorkPlanService {

	TaskDto saveWorkPlan(TaskDto task);

	Page<TaskDto> getListWorkPlan(int pageIndex, int pageSize);
	Page<TaskDto> getListWorkPlanByStep(Long stepId, int pageIndex, int pageSize);
	boolean deleteMultiple(TaskDto[] dtos);

	TaskDto getWorkPlansById(Long id);
	public Boolean addComment(TaskCommentDto comment);
}
