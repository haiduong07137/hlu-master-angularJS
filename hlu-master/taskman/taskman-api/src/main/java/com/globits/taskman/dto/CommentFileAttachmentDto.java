package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.taskman.domain.CommentFileAttachment;

public class CommentFileAttachmentDto extends BaseObjectDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8298625619192431396L;

	private FileDescriptionDto file;
	
	private TaskCommentDto taskComment;

	public FileDescriptionDto getFile() {
		return file;
	}

	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}

	public TaskCommentDto getTaskComment() {
		return taskComment;
	}

	public void setTaskComment(TaskCommentDto taskComment) {
		this.taskComment = taskComment;
	}
	public CommentFileAttachmentDto() {
		
	}
	
	public CommentFileAttachmentDto(CommentFileAttachment entity) {
		if(entity!=null) {
			if(entity.getFile()!=null) {
				this.file = new FileDescriptionDto();
				file.setContentSize(entity.getFile().getContentSize());
				file.setContentType(entity.getFile().getContentType());
				file.setName(entity.getFile().getName());
				file.setFilePath(entity.getFile().getFilePath());
			}
			
			if(entity.getTaskComment()!=null) {
				this.taskComment = new TaskCommentDto();
				this.taskComment.setComment(entity.getTaskComment().getComment());
				this.taskComment.setId(entity.getTaskComment().getId());
			}
		}
	}
}
