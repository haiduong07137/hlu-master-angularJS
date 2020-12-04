package com.globits.letter.dto;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocSecurityLevel;

public class LetterDocSecurityLevelDto extends BaseObjectDto {//Độ mật văn bản

	private static final long serialVersionUID = 1L;
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
	public LetterDocSecurityLevelDto() {
		
	}
	
	public LetterDocSecurityLevelDto(LetterDocSecurityLevel entity) {
		if(entity!=null) {
			this.name = entity.getName();
			this.code = entity.getCode();
		}
	}		
}
