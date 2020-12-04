package com.globits.cms.dto;

import com.globits.cms.domain.CmsArticleType;

public class CmsArticleTypeDto {
	private Long id;
	private String name;// Tên loại bài báo
	private String code;// Mã loại bài báo
	private String description;// Mô tả
	private int priority;
	
	private boolean isDuplicate;
	private String dupName;
	private String dupCode;
	

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public CmsArticleTypeDto() {
		super();
	}
	
	public CmsArticleTypeDto(CmsArticleType entity) {
		if(entity != null) {
			this.id = entity.getId();
			this.code = entity.getCode();
			this.name = entity.getName();
			this.description = entity.getDescription();
			this.priority = entity.getPriority();
		}
	}
	
	public CmsArticleType converCmsArticleTypeDto(CmsArticleTypeDto dto) {
		CmsArticleType at = new CmsArticleType();
		at.setId(dto.getId());
		at.setCode(dto.getCode());
		at.setName(dto.getName());
		at.setDescription(dto.getDescription());
		at.setPriority(dto.getPriority());
		return at;
	}
}
