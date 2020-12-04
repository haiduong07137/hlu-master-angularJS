package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.taskman.domain.TaskFileAttachment;

public class TaskFileAttachmentDto extends BaseObjectDto{
	private static final long serialVersionUID = 1L;
	private FileDescriptionDto file;
	private TaskDto task;
	
	public FileDescriptionDto getFile() {
		return file;
	}

	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}

	public TaskDto getTask() {
		return task;
	}

	public void setTask(TaskDto task) {
		this.task = task;
	}

	public TaskFileAttachmentDto() {
		
	}
	
	public TaskFileAttachmentDto(TaskFileAttachment entity) {
		if(entity!=null) {
			if(entity.getFile()!=null) {
				this.file = new FileDescriptionDto();
				file.setContentSize(entity.getFile().getContentSize());
				file.setContentType(entity.getFile().getContentType());
				file.setName(entity.getFile().getName());
				file.setFilePath(entity.getFile().getFilePath());
			}
			
			if(entity.getTask()!=null) {
				this.task = new TaskDto();
				this.task.setDescription(entity.getTask().getDescription());
				this.task.setId(entity.getTask().getId());
				this.task.setName(entity.getTask().getName());
				this.task.setDateDue(entity.getTask().getDateDue());
				this.task.setDateStart(entity.getTask().getDateStart());
				this.task.setSummary(entity.getTask().getSummary());
			}
		}
	}
}
