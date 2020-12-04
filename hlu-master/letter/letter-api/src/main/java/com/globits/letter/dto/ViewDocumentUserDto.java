package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.ViewDocumentUser;
import com.globits.security.dto.UserDto;

public class ViewDocumentUserDto extends BaseObjectDto {
	private UserDto user;
	private LetterDocumentDto letterDocument;
	
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public LetterDocumentDto getLetterDocument() {
		return letterDocument;
	}
	public void setLetterDocument(LetterDocumentDto letterDocument) {
		this.letterDocument = letterDocument;
	}
	public ViewDocumentUserDto() {
		
	}
	public ViewDocumentUserDto(ViewDocumentUser entity) {
		if(entity!=null) {
			this.id = entity.getId();
			this.createDate = entity.getCreateDate();
			if (entity.getUser() != null) {
				this.user = new UserDto();
				this.user.setId(entity.getUser().getId());
				this.user.setUsername(entity.getUser().getUsername());
				this.user.setEmail(entity.getUser().getEmail());
				if(entity.getUser().getPerson()!=null && entity.getUser().getPerson().getDisplayName()!=null) {
					this.user.setDisplayName(entity.getUser().getPerson().getDisplayName());
				}
			}
			if (entity.getLetterDocument() != null) {
				this.letterDocument = new LetterDocumentDto();
				this.letterDocument.setId(entity.getLetterDocument().getId());
			}
		}
	}
}
