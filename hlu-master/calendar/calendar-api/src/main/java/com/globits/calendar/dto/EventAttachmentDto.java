package com.globits.calendar.dto;

import com.globits.calendar.domain.Event;
import com.globits.calendar.domain.EventAttachment;
import com.globits.core.domain.FileDescription;
import com.globits.core.dto.AuditableEntityDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.utils.CommonUtils;

public class EventAttachmentDto extends AuditableEntityDto {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long fileId;
	
	private Long eventId;
	private FileDescriptionDto file;
	
	public EventAttachmentDto() {

	}

	public EventAttachmentDto(EventAttachment entity, boolean includeContent) {
		if (entity == null) {
			return;
		}

		id = entity.getId();

		if (entity.getEvent() != null) {
			eventId = entity.getEvent().getId();
		}
		if(entity.getFile()!=null) {
			this.fileId = entity.getFile().getId();
			this.file = new FileDescriptionDto(entity.getFile());
		}
		setCreateDate(entity.getCreateDate());
		setCreatedBy(entity.getCreatedBy());
		setModifyDate(entity.getModifyDate());
		setModifiedBy(entity.getModifiedBy());
	}

	public EventAttachment toEntity() {

		EventAttachment entity = new EventAttachment();

		entity.setId(id);

		if (CommonUtils.isPositive(eventId, true)) {
			Event event = new Event();
			event.setId(eventId);

			entity.setEvent(event);
		}
		if(this.getFile()!=null) {
			FileDescription file = new FileDescription();
			file.setId(this.getFile().getId());
			if(this.getFile().getId()==null) {
				file.setContentSize(this.getFile().getContentSize());
				file.setContentType(this.getFile().getContentType());
				file.setName(this.getFile().getName());
				file.setFilePath(this.getFile().getFilePath());
			}
			entity.setFile(file);
		}
		entity.setCreateDate(getCreateDate());
		entity.setCreatedBy(getCreatedBy());

		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getContentType() {
//		return contentType;
//	}
//
//	public void setContentType(String contentType) {
//		this.contentType = contentType;
//	}
//
//	public Long getContentLength() {
//		return contentLength;
//	}
//
//	public void setContentLength(Long contentLength) {
//		this.contentLength = contentLength;
//	}
//
//	public String getExtension() {
//		return extension;
//	}
//
//	public void setExtension(String extension) {
//		this.extension = extension;
//	}
//
//	public byte[] getContent() {
//		return content;
//	}
//
//	public void setContent(byte[] content) {
//		this.content = content;
//	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public FileDescriptionDto getFile() {
		return file;
	}

	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
