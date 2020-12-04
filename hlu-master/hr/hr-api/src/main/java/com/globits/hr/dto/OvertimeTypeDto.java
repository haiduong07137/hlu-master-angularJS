/*
 * Created by TA & Giang on 22/4/2018.
 */

package com.globits.hr.dto;

import java.io.Serializable;

import com.globits.hr.domain.OvertimeType;
import com.globits.hr.domain.Position;
import com.globits.hr.domain.PositionTitle;

public class OvertimeTypeDto implements Serializable {

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
	public OvertimeTypeDto(){}
	
	
	public OvertimeTypeDto(OvertimeType ov){
		
		if (ov!=null) {
			this.setId(ov.getId());
			this.setCode(ov.getCode());			
			this.setName(ov.getName());
		}
	}
	
}
