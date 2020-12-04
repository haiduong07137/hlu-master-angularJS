package com.globits.letter.dto;

import com.globits.core.dto.BaseObjectDto;

public class UserRolesDto extends BaseObjectDto {
	
	private Long taskId;				//

	private Boolean hasClerkRole;		//Vai trò văn thư
	
	private Boolean hasFowardRole;		//Vai trò phân luồng
	
	private Boolean hasAssignerRole;	//Vai trò giao xử lý
	
	private Boolean hasChairmanRole;	//Vai trò chủ trì
	
	private Boolean hasProcessRole;		//Vai trò tham gia
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Boolean getHasClerkRole() {
		return hasClerkRole;
	}
	public void setHasClerkRole(Boolean hasClerkRole) {
		this.hasClerkRole = hasClerkRole;
	}
	public Boolean getHasFowardRole() {
		return hasFowardRole;
	}
	public void setHasFowardRole(Boolean hasFowardRole) {
		this.hasFowardRole = hasFowardRole;
	}
	public Boolean getHasAssignerRole() {
		return hasAssignerRole;
	}
	public void setHasAssignerRole(Boolean hasAssignerRole) {
		this.hasAssignerRole = hasAssignerRole;
	}
	public Boolean getHasChairmanRole() {
		return hasChairmanRole;
	}
	public void setHasChairmanRole(Boolean hasChairmanRole) {
		this.hasChairmanRole = hasChairmanRole;
	}
	public Boolean getHasProcessRole() {
		return hasProcessRole;
	}
	public void setHasProcessRole(Boolean hasProcessRole) {
		this.hasProcessRole = hasProcessRole;
	}
}
