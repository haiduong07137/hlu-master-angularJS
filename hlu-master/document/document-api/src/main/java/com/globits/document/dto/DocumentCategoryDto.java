package com.globits.document.dto;

import com.globits.document.domain.DocumentCategory;

public class DocumentCategoryDto extends BaseObjectDto{
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
	
	public DocumentCategoryDto() {

	}
	
	public DocumentCategoryDto(DocumentCategory entity) {
		if (entity != null) {
			this.setId(entity.getId());
			this.code = entity.getCode();
			this.name = entity.getName();
		}
	}
}
