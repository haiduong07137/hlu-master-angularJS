package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.taskman.domain.TaskRole;

public class TaskRoleDto extends BaseObjectDto{
	private String name;
	private String code;
	
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
	
	public TaskRoleDto() {
		
	}
	
	public TaskRoleDto(TaskRole domain) {
		this.name = domain.getName();
		this.code = domain.getCode();
		this.setId(domain.getId());
	}
}