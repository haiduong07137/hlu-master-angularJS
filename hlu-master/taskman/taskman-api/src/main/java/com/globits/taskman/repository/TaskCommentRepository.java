package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.dto.TaskCommentDto;
@Repository
public interface TaskCommentRepository  extends JpaRepository<TaskComment, Long> {
	@Query("select new com.globits.taskman.dto.TaskCommentDto(d) from TaskComment d")
	public Page<TaskCommentDto> getListTaskComment(Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.TaskCommentDto(d) from TaskComment d where d.participate.task.id=?1")
	public Page<TaskCommentDto> getListTaskCommentByTaskId(Long taskId, Pageable pageable);
}
