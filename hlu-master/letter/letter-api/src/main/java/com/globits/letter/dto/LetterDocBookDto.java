package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.domain.LetterDocBookGroup;

public class LetterDocBookDto  extends BaseObjectDto{
	private String name;
	private String code;
	private String docAppType;//Là ứng dụng nào - InDocument, OutDocument, ...
	private LetterDocBookGroupDto docBookGroup;	//nhóm vào sổ văn bản
	
	private boolean isDuplicate;
	private String dupName;
	private String dupCode;
	private Integer currentNumber;
	
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
	
	public String getDocAppType() {
		return docAppType;
	}
	public void setDocAppType(String docAppType) {
		this.docAppType = docAppType;
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
	
	public LetterDocBookGroupDto getDocBookGroup() {
		return docBookGroup;
	}
	public void setDocBookGroup(LetterDocBookGroupDto docBookGroup) {
		this.docBookGroup = docBookGroup;
	}
	
	public Integer getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}
	public LetterDocBookDto() {
		
	}
	public LetterDocBookDto(LetterDocBook entity) {
		this.id = entity.getId();
		this.code = entity.getCode();
		this.name = entity.getName();
		this.docBookGroup = new LetterDocBookGroupDto(entity.getDocBookGroup());
		this.currentNumber = entity.getCurrentNumber();
	}
}
