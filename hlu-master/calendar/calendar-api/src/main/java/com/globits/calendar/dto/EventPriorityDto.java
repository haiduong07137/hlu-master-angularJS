package com.globits.calendar.dto;

import com.globits.calendar.domain.EventPriority;
import com.globits.core.dto.AuditableEntityDto;

public class EventPriorityDto extends AuditableEntityDto {

	private Long id;

	private String name;

	private String description;

	private Integer priority;

	public EventPriorityDto() {

	}

	public EventPriorityDto(EventPriority entity) {
		if (entity == null) {
			return;
		}
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.priority = entity.getPriority();
		this.description = entity.getDescription();
		
		setCreateDate(entity.getCreateDate());
		setCreatedBy(entity.getCreatedBy());
		setModifyDate(entity.getModifyDate());
		setModifiedBy(entity.getModifiedBy());
	}

	public EventPriority toEntity() {
		
		EventPriority entity = new EventPriority();
		
		entity.setId(id);
		entity.setPriority(priority);
		entity.setName(name);
		entity.setDescription(description);
		
		entity.setCreateDate(getCreateDate());
		entity.setCreatedBy(getCreatedBy());
		
		return entity;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
