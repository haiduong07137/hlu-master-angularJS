package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocPriority;

public class LetterDocPriorityDto extends BaseObjectDto {
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
	public LetterDocPriorityDto() {
		
	}
	
	public LetterDocPriorityDto(LetterDocPriority entity) {
		if(entity!=null) {
			this.name = entity.getName();
			this.code = entity.getCode();
		}
	}	
	
}
