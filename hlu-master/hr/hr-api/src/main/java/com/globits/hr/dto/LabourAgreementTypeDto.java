package com.globits.hr.dto;

import java.io.Serializable;

import com.globits.hr.domain.LabourAgreementType;

public class LabourAgreementTypeDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String code;
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
	public LabourAgreementTypeDto() {}
	
	public LabourAgreementTypeDto( LabourAgreementType labourAgreement) {
		if(labourAgreement != null) {
			this.setId(labourAgreement.getId());
			this.setName(labourAgreement.getName());
			this.setCode(labourAgreement.getCode());
		}
	}
}
