package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.security.dto.UserDto;
import com.globits.taskman.domain.UserTaskOwner;

public class UserTaskOwnerDto extends BaseObjectDto{
	private TaskOwnerDto taskOwner;
	private UserDto user;
	private TaskRoleDto role;
	
	public TaskOwnerDto getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(TaskOwnerDto taskOwner) {
		this.taskOwner = taskOwner;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public TaskRoleDto getRole() {
		return role;
	}

	public void setRole(TaskRoleDto role) {
		this.role = role;
	}

	public UserTaskOwnerDto() {
		
	}
	
	public UserTaskOwnerDto(UserTaskOwner entity) {
		if(entity.getUser()!=null) {
			this.user = new UserDto();
			this.user.setId(entity.getUser().getId());
			this.user.setUsername(entity.getUser().getUsername());
		}
		if(entity.getTaskOwner()!=null) {
			this.taskOwner = new TaskOwnerDto();
			this.taskOwner.setId(entity.getTaskOwner().getId());
			this.taskOwner.setDisplayName(entity.getTaskOwner().getDisplayName());
			this.taskOwner.setOwnerType(entity.getTaskOwner().getOwnerType());
			if(entity.getTaskOwner().getDepartment()!=null) {
				this.taskOwner.setDepartment(new DepartmentDto());
				this.taskOwner.getDepartment().setId(entity.getTaskOwner().getDepartment().getId());
				this.taskOwner.getDepartment().setCode(entity.getTaskOwner().getDepartment().getCode());
				this.taskOwner.getDepartment().setName(entity.getTaskOwner().getDepartment().getName());
			}
		}
		if(entity.getRole()!=null) {
			role = new TaskRoleDto(entity.getRole());	
		}		
	}
}
