package com.globits.taskman.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.dto.TaskCommentDto;

public interface TaskCommentService extends GenericService<TaskComment, Long> {
	public TaskCommentDto getTaskComment(Long id);
	public TaskCommentDto saveTaskComment(TaskCommentDto dto);
	public Boolean deleteTaskComment(Long id);
	public Page<TaskCommentDto> getListTaskComment(int pageSize, int pageIndex);
	Page<TaskCommentDto> getListTaskCommentByTaskId(Long taskId, int pageSize, int pageIndex);
	TaskCommentDto addEditTaskComment(TaskCommentDto dto);
}
