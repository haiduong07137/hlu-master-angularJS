package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.taskman.domain.TaskStep;

public class TaskStepDto extends BaseObjectDto{
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
	
	public TaskStepDto() {
		
	}
	
	public TaskStepDto(TaskStep docCategory) {
		this.name = docCategory.getName();
		this.code = docCategory.getCode();
		this.setId(docCategory.getId());
	}
}
