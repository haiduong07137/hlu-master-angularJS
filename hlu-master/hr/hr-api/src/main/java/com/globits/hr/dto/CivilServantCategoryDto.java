package com.globits.hr.dto;

import com.globits.hr.domain.CivilServantCategory;

public class CivilServantCategoryDto {
	
	private static final long serialVersionUID = 1L;	
	
	private Long id;
	
	private String name;

	private String code;

	private Boolean voided;
	
	

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

	public Boolean getVoided() {
		return voided;
	}

	public void setVoided(Boolean voided) {
		this.voided = voided;
	}

	public CivilServantCategoryDto() {}
	
	public CivilServantCategoryDto(CivilServantCategory civilServantCategory) {
		if(civilServantCategory!=null) {
			this.setId(civilServantCategory.getId());
			this.setName(civilServantCategory.getName());
			this.setCode(civilServantCategory.getCode());
			this.setVoided(civilServantCategory.getVoided());
		}
	}

}