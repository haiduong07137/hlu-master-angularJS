package com.globits.hr.dto;

import java.io.Serializable;

import com.globits.core.domain.Department;
import com.globits.hr.domain.Position;
import com.globits.hr.domain.PositionTitle;

public class PositionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private int status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public PositionDto(){
		
	}
	public PositionDto(Position p){
		this.description=p.getDescription();
		this.name=p.getName();
		this.status=p.getStatus();
	}
}
