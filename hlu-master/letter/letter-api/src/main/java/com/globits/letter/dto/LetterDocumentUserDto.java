package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocumentUser;
import com.globits.security.dto.UserDto;

public class LetterDocumentUserDto extends BaseObjectDto {
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
	
	public LetterDocumentUserDto() {
		
	}
	
	public LetterDocumentUserDto(LetterDocumentUser entity) {
		if(entity!=null) {
			if(entity.getUser()!=null) {
				this.user = new UserDto();
				this.user.setActive(entity.getUser().getActive());
				this.user.setId(entity.getUser().getId());
				this.user.setUsername(entity.getUser().getUsername());				
			}
			if(entity.getLetterDocument()!=null) {
				this.letterDocument = new LetterDocumentDto();
				this.letterDocument.setId(entity.getLetterDocument().getId());
				this.letterDocument.setBriefNote(entity.getLetterDocument().getBriefNote());
			}
			
		}
	}
}
