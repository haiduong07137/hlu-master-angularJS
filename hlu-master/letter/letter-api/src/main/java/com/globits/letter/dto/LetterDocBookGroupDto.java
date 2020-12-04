package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocBookGroup;

public class LetterDocBookGroupDto extends BaseObjectDto {
	private String name;
	private String code;
	
	private boolean isDuplicate;
	private String dupName;
	private String dupCode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean isDuplicate() {
		return isDuplicate;
	}
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
	public String getDupName() {
		return dupName;
	}
	public void setDupName(String dupName) {
		this.dupName = dupName;
	}
	public String getDupCode() {
		return dupCode;
	}
	public void setDupCode(String dupCode) {
		this.dupCode = dupCode;
	}
	public LetterDocBookGroupDto() {
	}

	public LetterDocBookGroupDto(LetterDocBookGroup entity) {
		this.id = entity.getId();
		this.code = entity.getCode();
		this.name = entity.getName();
	}

}
