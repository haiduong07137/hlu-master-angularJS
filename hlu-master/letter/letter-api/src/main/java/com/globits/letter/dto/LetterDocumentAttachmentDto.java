package com.globits.letter.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.LocalDateTime;

import com.globits.core.domain.FileDescription;
import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.dto.OrganizationDto;
import com.globits.core.utils.CommonUtils;
import com.globits.letter.domain.LetterDocument;
import com.globits.letter.domain.LetterDocumentAttachment;
import com.globits.letter.domain.LetterInDocument;
import com.globits.taskman.dto.TaskDto;


public class LetterDocumentAttachmentDto extends BaseObjectDto{
	private static final long serialVersionUID = 1L;
	private Long id;
	private FileDescriptionDto file;
	private LetterDocumentDto document;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FileDescriptionDto getFile() {
		return file;
	}

	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}

	public LetterDocumentDto getDocument() {
		return document;
	}

	public void setDocument(LetterDocumentDto document) {
		this.document = document;
	}
	public LetterDocumentAttachmentDto() {
		
	}

	public LetterDocumentAttachmentDto(LetterDocumentAttachment entity) {
		if(entity!=null) {
			this.setId(entity.getId());
			if(entity.getFile()!=null) {
				this.file = new FileDescriptionDto(entity.getFile());
			}
			if(entity.getDocument() != null) {
				this.setDocument(new LetterDocumentDto(entity.getDocument()));
			}
		}
	}
}
