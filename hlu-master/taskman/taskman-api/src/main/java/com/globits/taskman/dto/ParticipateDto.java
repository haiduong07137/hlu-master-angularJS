package com.globits.taskman.dto;

import java.util.HashSet;
import java.util.Set;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.hr.dto.StaffDto;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.TaskComment;

public class ParticipateDto extends BaseObjectDto{
	private String displayName;
	private DepartmentDto department;
	private StaffDto employee;
	private int participateType;
	private Integer currentState;
	private TaskDto task;
	private TaskRoleDto role;
	private Set<TaskCommentDto> comments;
	private TaskOwnerDto taskOwner;
	private Boolean hasOwnerPermission = false;//Dùng để xác định xem người dùng hiện tại có quyền hay không.
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}

	public StaffDto getEmployee() {
		return employee;
	}

	public void setEmployee(StaffDto employee) {
		this.employee = employee;
	}

	public int getParticipateType() {
		return participateType;
	}

	public void setParticipateType(int participateType) {
		this.participateType = participateType;
	}

	public Integer getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}

	public TaskDto getTask() {
		return task;
	}

	public void setTask(TaskDto task) {
		this.task = task;
	}

	public TaskRoleDto getRole() {
		return role;
	}

	public void setRole(TaskRoleDto role) {
		this.role = role;
	}

	public Set<TaskCommentDto> getComments() {
		return comments;
	}

	public void setComments(Set<TaskCommentDto> comments) {
		this.comments = comments;
	}

	public TaskOwnerDto getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(TaskOwnerDto taskOwner) {
		this.taskOwner = taskOwner;
	}

	public Boolean getHasOwnerPermission() {
		return hasOwnerPermission;
	}

	public void setHasOwnerPermission(Boolean hasOwnerPermission) {
		this.hasOwnerPermission = hasOwnerPermission;
	}

	public ParticipateDto() {
		
	}
	
	public ParticipateDto(Participate domain) {
		this.setId(domain.getId());
		this.displayName = domain.getDisplayName();
		this.participateType=domain.getParticipateType();
		this.currentState = domain.getCurrentState();
		this.department =  new DepartmentDto();
		//Thêm code ở đây để set giá trị vào DTO
		this.employee= new StaffDto();
		//Thêm đoạn code để set giá trị cho employee ở đây
		this.task=new TaskDto();
		//Thêm đoạn code để set giá trị cho task ở đây		
		this.role=new TaskRoleDto();
		//Thêm đoạn code để set giá trị cho Role ở đây
		if(domain.getTaskOwner()!=null) {
			this.taskOwner = new TaskOwnerDto();
			this.taskOwner.setId(domain.getTaskOwner().getId());
			this.taskOwner.setDisplayName(domain.getTaskOwner().getDisplayName());
			this.taskOwner.setOwnerType(domain.getTaskOwner().getOwnerType());
		}
		
		
		//Thực hiện liệt kê các comment của Participate này
		if(domain.getComments()!=null) {
			this.setComments(new HashSet<TaskCommentDto>());
			for(TaskComment m : domain.getComments()) {
				TaskCommentDto mDto = new TaskCommentDto();
				mDto.setComment(m.getComment());
				mDto.setId(m.getId());
				this.getComments().add(mDto);
			}
		}
	}
}
