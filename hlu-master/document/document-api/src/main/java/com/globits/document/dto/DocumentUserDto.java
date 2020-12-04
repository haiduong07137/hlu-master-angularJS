package com.globits.document.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.document.domain.DocumentUser;
import com.globits.document.dto.DocumentDto;
import com.globits.security.dto.UserDto;

public class DocumentUserDto extends BaseObjectDto{
	private UserDto user;
	private DocumentDto document;
	
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public DocumentDto getDocument() {
		return document;
	}
	public void setDocument(DocumentDto document) {
		this.document = document;
	}
	public DocumentUserDto() {
		
	}


	public DocumentUserDto(DocumentUser entity) {
		if(entity!=null) {
			if(entity.getUser()!=null) {
				this.user = new UserDto();
				this.user.setActive(entity.getUser().getActive());
				this.user.setId(entity.getUser().getId());
				this.user.setUsername(entity.getUser().getUsername());				
			}
			if(entity.getDocument()!=null) {
				this.document = new DocumentDto();
				this.document.setId(entity.getDocument().getId());
				this.document.setTitle(entity.getDocument().getTitle());
			}
			
		}
	}
}
