package com.globits.document.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.document.domain.DocumentAttachment;
import com.globits.document.dto.DocumentDto;

public class DocumentAttachmentDto extends BaseObjectDto{
	private FileDescriptionDto file;
	private DocumentDto document;

	public FileDescriptionDto getFile() {
		return file;
	}

	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}

	public DocumentDto getDocument() {
		return document;
	}

	public void setDocument(DocumentDto document) {
		this.document = document;
	}
	public DocumentAttachmentDto() {
		
	}

	public DocumentAttachmentDto(DocumentAttachment entity) {
		if(entity!=null) {
			this.setId(entity.getId());
			if(entity.getFile()!=null) {
				this.file = new FileDescriptionDto(entity.getFile());
			}
			if(entity.getDocument() != null) {
				this.setDocument(new DocumentDto(entity.getDocument()));
			}
		}
	}

}
