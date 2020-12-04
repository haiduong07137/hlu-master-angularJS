/*
 * Created by TA2 & Giang on 23/4/2018.
 */

package com.globits.hr.dto;

import java.io.Serializable;

import com.globits.hr.domain.SalaryIncrementType;


public class SalaryIncrementTypeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String code;
	private String name;
	
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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

	public SalaryIncrementTypeDto() {}
	


	public SalaryIncrementTypeDto(SalaryIncrementType sity){
		if(sity!=null) {
			this.setId(sity.getId());
			this.setName(sity.getName());
			this.setCode(sity.getCode());
		}	
				
	}
}
