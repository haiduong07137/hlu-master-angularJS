package com.globits.document.dto;

import java.util.ArrayList;
import java.util.List;

import com.globits.core.dto.FileDescriptionDto;
import com.globits.document.domain.Document;
import com.globits.document.domain.DocumentAttachment;
import com.globits.document.domain.DocumentCategory;
import com.globits.document.domain.DocumentUser;

public class DocumentDto extends BaseObjectDto{
	private String title;
	private String description;
	private String docCode;
	private String summary;
	private Boolean isLimitedRead;
	
	private DocumentCategoryDto category;
	
	private List<DocumentAttachmentDto> attachments = new ArrayList<DocumentAttachmentDto>();
	
	private List<DocumentUserDto> userPermission;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public DocumentCategoryDto getCategory() {
		return category;
	}

	public void setCategory(DocumentCategoryDto category) {
		this.category = category;
	}

	public List<DocumentAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DocumentAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public List<DocumentUserDto> getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(List<DocumentUserDto> userPermission) {
		this.userPermission = userPermission;
	}

	public Boolean getIsLimitedRead() {
		return isLimitedRead;
	}

	public void setIsLimitedRead(Boolean isLimitedRead) {
		this.isLimitedRead = isLimitedRead;
	}

	public DocumentDto() {

	}
	
	public DocumentDto(Document entity) {
		if (entity != null) {
			this.setId(entity.getId());
			this.title = entity.getTitle();
			this.description = entity.getDescription();
			this.docCode = entity.getDocCode();
			this.summary = entity.getSummary();
			this.isLimitedRead = entity.getIsLimitedRead();
			this.category = new DocumentCategoryDto(entity.getCategory());
			
			if (entity.getAttachments() != null) {
				for (DocumentAttachment att : entity.getAttachments()) {
					DocumentAttachmentDto attDto = new DocumentAttachmentDto();
					attDto.setDocument(new DocumentDto());
					attDto.getDocument().setDocCode(att.getDocument().getDocCode());
					attDto.getDocument().setId(att.getDocument().getId());

					if (att.getFile() != null) {
						FileDescriptionDto fileDescription = new FileDescriptionDto(att.getFile());
						attDto.setFile(fileDescription);
					}
					this.getAttachments().add(attDto);
				}
			}
			if(entity.getUserPermission()!=null) {
				this.userPermission = new ArrayList<DocumentUserDto>();
				for(DocumentUser u : entity.getUserPermission()) {
					DocumentUserDto uDto = new DocumentUserDto(u);
					this.userPermission.add(uDto);
				}
			}
		}
	}
}
