package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.taskman.domain.ProjectTaskOwner;

public class ProjectTaskOwnerDto extends BaseObjectDto{
	private ProjectDto project;
	private TaskOwnerDto taskOwner;
	private TaskRoleDto mainRole;
	public ProjectDto getProject() {
		return project;
	}
	public void setProject(ProjectDto project) {
		this.project = project;
	}
	public TaskOwnerDto getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(TaskOwnerDto taskOwner) {
		this.taskOwner = taskOwner;
	}
	public TaskRoleDto getMainRole() {
		return mainRole;
	}
	public void setMainRole(TaskRoleDto mainRole) {
		this.mainRole = mainRole;
	}
	public ProjectTaskOwnerDto() {
		
	}
	
	public ProjectTaskOwnerDto(ProjectTaskOwner entity) {
		if(entity.getProject()!=null) {
			this.project = new ProjectDto();
			this.project.setId(entity.getProject().getId());
			this.project.setCode(entity.getProject().getCode());
			this.project.setName(entity.getProject().getName());
		}
		
		if(entity.getTaskOwner()!=null) {
			this.taskOwner = new TaskOwnerDto();
			this.taskOwner.setDisplayName(entity.getTaskOwner().getDisplayName());
			this.taskOwner.setId(entity.getTaskOwner().getId());
			this.taskOwner.setOwnerType(entity.getTaskOwner().getOwnerType());
		}
		
		if(entity.getMainRole()!=null) {
			this.mainRole = new TaskRoleDto();
			this.mainRole.setCode(entity.getMainRole().getCode());
			this.mainRole.setName(entity.getMainRole().getName());
			this.mainRole.setId(entity.getMainRole().getId());
		}
	}
	
}
