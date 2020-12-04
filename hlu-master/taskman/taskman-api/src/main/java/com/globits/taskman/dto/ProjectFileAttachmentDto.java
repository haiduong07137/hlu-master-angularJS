package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;

public class ProjectFileAttachmentDto extends BaseObjectDto{
	private FileDescriptionDto file;
	private ProjectDto project;
	
	public FileDescriptionDto getFile() {
		return file;
	}
	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}
	public ProjectDto getProject() {
		return project;
	}
	public void setProject(ProjectDto project) {
		this.project = project;
	}

}
