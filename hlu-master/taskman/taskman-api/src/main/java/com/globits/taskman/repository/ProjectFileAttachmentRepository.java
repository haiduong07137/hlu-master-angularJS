package com.globits.taskman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.globits.taskman.domain.ProjectFileAttachment;

public interface ProjectFileAttachmentRepository extends JpaRepository<ProjectFileAttachment, Long>{

}
