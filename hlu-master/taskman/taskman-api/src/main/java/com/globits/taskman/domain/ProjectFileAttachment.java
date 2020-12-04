package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;

@Entity
@Table(name = "tbl_task_file_attachment")
public class ProjectFileAttachment extends BaseObject{

	@Transient
	private static final long serialVersionUID = -8875313686216064164L;

	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public FileDescription getFile() {
		return file;
	}

	public void setFile(FileDescription file) {
		this.file = file;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	
}
