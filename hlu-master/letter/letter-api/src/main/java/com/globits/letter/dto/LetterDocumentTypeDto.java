package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocumentType;

public class LetterDocumentTypeDto  extends BaseObjectDto{//Loại văn bản
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public LetterDocumentTypeDto() {
		
	}
	
	public LetterDocumentTypeDto(LetterDocumentType entity) {
		if(entity!=null) {
			this.name = entity.getName();
			this.code = entity.getCode();
		}
	}	
}
