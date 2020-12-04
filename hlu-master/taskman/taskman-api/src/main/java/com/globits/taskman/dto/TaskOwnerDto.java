package com.globits.taskman.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.security.dto.UserDto;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.UserTaskOwner;

public class TaskOwnerDto extends BaseObjectDto {
	private String displayName;
	private Integer ownerType;//0= phòng ban, 1= cá nhân, other type = 2 - dùng TaskOwnerTypeEnum để lấy giá trị
	private TaskOwnerDto parent;
	private DepartmentDto department;
	private PersonDto person;
	private List<UserTaskOwnerDto> userTaskOwners;
	private List<TaskOwnerDto> children;
	private Boolean haveChil = false;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	public TaskOwnerDto getParent() {
		return parent;
	}

	public void setParent(TaskOwnerDto parent) {
		this.parent = parent;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}

	public List<UserTaskOwnerDto> getUserTaskOwners() {
		return userTaskOwners;
	}

	public void setUserTaskOwners(List<UserTaskOwnerDto> userTaskOwners) {
		this.userTaskOwners = userTaskOwners;
	}

	public List<TaskOwnerDto> getChildren() {
		return children;
	}

	public void setChildren(List<TaskOwnerDto> children) {
		this.children = children;
	}
	

	public Boolean getHaveChil() {
		return haveChil;
	}

	public void setHaveChil(Boolean haveChil) {
		this.haveChil = haveChil;
	}

	public TaskOwnerDto() {
		
	}
	
	public TaskOwnerDto(TaskOwner entity) {
		if(entity!=null) {
			this.displayName = entity.getDisplayName();
			this.ownerType = entity.getOwnerType();
			this.setId(entity.getId());
			if(entity.getParent()!=null) {
				this.parent = new TaskOwnerDto();
				this.parent.setId(entity.getParent().getId());
				this.parent.setDisplayName(entity.getParent().getDisplayName());
				this.parent.setOwnerType(entity.getParent().getOwnerType());		
			}
	
			if(entity.getPerson()!=null) {
				this.person = new PersonDto(entity.getPerson());
			}
			if(entity.getDepartment()!=null) {
				this.department = new DepartmentDto(entity.getDepartment());
			}
			if(entity.getUserTaskOwners()!=null && entity.getUserTaskOwners().size()>0) {
				this.userTaskOwners = new ArrayList<UserTaskOwnerDto>();
				for(UserTaskOwner u : entity.getUserTaskOwners()) {
					UserTaskOwnerDto utoDto = new UserTaskOwnerDto();
					utoDto.setCreateDate(u.getCreateDate());
					utoDto.setId(u.getId());
					if(u.getUser() != null)
						utoDto.setUser(new UserDto(u.getUser()));
					if(u.getRole() != null)
						utoDto.setRole(new TaskRoleDto(u.getRole()));
					userTaskOwners.add(utoDto);
				}
			}
			this.setChildren(getChildrenTaskOwner(entity));
		}
	}

	private List<TaskOwnerDto> getChildrenTaskOwner(TaskOwner entity) {
		List<TaskOwnerDto> results = new ArrayList<TaskOwnerDto>();
		if(entity.getSubTaskOwner() != null && entity.getSubTaskOwner().size() > 0) {
			for(TaskOwner taskOwner: entity.getSubTaskOwner()) {
				TaskOwnerDto taskOwnerDto = new TaskOwnerDto();
				taskOwnerDto.setId(taskOwner.getId());
				taskOwnerDto.setDisplayName(taskOwner.getDisplayName());
				taskOwnerDto.setOwnerType(taskOwner.getOwnerType());
				
				if(taskOwner.getParent() != null) {
					taskOwnerDto.setParent(new TaskOwnerDto());
					TaskOwnerDto parentSub = new TaskOwnerDto();
					parentSub.setId(taskOwner.getParent().getId());
					parentSub.setDisplayName(taskOwner.getParent().getDisplayName());
					parentSub.setOwnerType(taskOwner.getParent().getOwnerType());
					if(taskOwner.getParent().getPerson() != null) {
						parentSub.setPerson(new PersonDto(taskOwner.getParent().getPerson()));
					}
				}
				taskOwnerDto.setChildren(getChildrenTaskOwner(taskOwner));
				results.add(taskOwnerDto);
				this.setHaveChil(results!=null && results.size()>0);
			}
			return results;
		}
		return null;
	}
	
	
	
}
