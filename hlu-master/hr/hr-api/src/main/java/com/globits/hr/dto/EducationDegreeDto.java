package com.globits.hr.dto;
/*
 * author Giang-Tuan Anh
 */
import java.io.Serializable;

import com.globits.hr.domain.EducationDegree;

public class EducationDegreeDto implements Serializable{
	private static final long serialVersionUTD=1L;
	
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
	
	public EducationDegreeDto() {}// thiếu sẽ báo lỗi 400 khi post dữ liệu
	
	public EducationDegreeDto(EducationDegree educationDegree) {
		if (educationDegree!=null) {
			this.setId(educationDegree.getId());
			this.setCode(educationDegree.getCode());
			this.setName(educationDegree.getName());
		}
	}
}
