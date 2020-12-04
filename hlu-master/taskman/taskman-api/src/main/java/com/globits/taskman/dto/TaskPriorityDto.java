package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.taskman.domain.TaskPriority;

public class TaskPriorityDto extends BaseObjectDto{
	private String name;
	private String code;
	private int priorityOrder;
	
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
	
	public int getPriorityOrder() {
		return priorityOrder;
	}
	public void setPriorityOrder(int priorityOrder) {
		this.priorityOrder = priorityOrder;
	}
	public TaskPriorityDto() {
		
	}
	
	public TaskPriorityDto(TaskPriority domain) {
		this.setId(domain.getId());
		this.name = domain.getName();
		this.code = domain.getCode();
		this.priorityOrder = domain.getPriorityOrder();
	}
}
