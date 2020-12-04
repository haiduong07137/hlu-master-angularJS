package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;

@Entity
@Table(name = "tbl_comment_file_attachment")
public class CommentFileAttachment extends BaseObject{

	@Transient
	private static final long serialVersionUID = -8875313686216064164L;

	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	
	@ManyToOne
	@JoinColumn(name = "task_comment_id")
	private TaskComment taskComment;

	public FileDescription getFile() {
		return file;
	}

	public void setFile(FileDescription file) {
		this.file = file;
	}

	public TaskComment getTaskComment() {
		return taskComment;
	}

	public void setTaskComment(TaskComment taskComment) {
		this.taskComment = taskComment;
	}

	
}
