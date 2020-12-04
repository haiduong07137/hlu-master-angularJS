package com.globits.taskman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskFileAttachment;
@Repository
public interface TaskFileAttachmentRepository  extends JpaRepository<TaskFileAttachment, Long> {

}
