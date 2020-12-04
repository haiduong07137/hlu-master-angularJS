package com.globits.taskman.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.taskman.domain.ProjectFileAttachment;
import com.globits.taskman.service.ProjectFileAttachmentService;

@Service
@Transactional
public class ProjectFileAttachmentServiceImpl extends GenericServiceImpl<ProjectFileAttachment, Long> implements ProjectFileAttachmentService{

}
