package com.globits.hr.dto;

import com.globits.hr.domain.PositionTitle;

public class PositionTitleDto {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private String description;
	
	private Integer type;

	
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public PositionTitleDto() {}
	
	public PositionTitleDto(PositionTitle title) {
		if(title!=null) {
			this.setId(title.getId());
			this.setName(title.getName());
			this.setCode(title.getCode());
			this.setDescription(title.getDescription());
			this.setType(title.getType());
	}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}