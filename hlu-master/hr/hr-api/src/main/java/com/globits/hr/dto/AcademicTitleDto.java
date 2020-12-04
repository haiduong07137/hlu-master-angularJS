/*
 * TA va Giang làm
 */

package com.globits.hr.dto;

import java.io.Serializable;

import com.globits.core.domain.Department;
import com.globits.hr.domain.AcademicTitle;
import com.globits.hr.domain.Position;
import com.globits.hr.domain.PositionTitle;

public class AcademicTitleDto implements Serializable {

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
	public AcademicTitleDto(){
		
	}
	public AcademicTitleDto(AcademicTitle ac){
		
		if (ac!=null) {
			this.setId(ac.getId());
			this.setCode(ac.getCode());			
			this.setName(ac.getName());
		}
	}
	
}
