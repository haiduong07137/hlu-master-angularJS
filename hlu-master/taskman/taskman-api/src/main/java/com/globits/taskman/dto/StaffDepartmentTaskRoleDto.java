package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.hr.dto.StaffDto;
import com.globits.taskman.domain.StaffDepartmentTaskRole;

public class StaffDepartmentTaskRoleDto extends BaseObjectDto {
	private StaffDto staff;//Nhân viên nào
	private TaskRoleDto role;//Được xử lý việc gì
	private DepartmentDto department;//Cho phòng ban nào
	public StaffDto getStaff() {
		return staff;
	}
	public void setStaff(StaffDto staff) {
		this.staff = staff;
	}
	public TaskRoleDto getRole() {
		return role;
	}
	public void setRole(TaskRoleDto role) {
		this.role = role;
	}
	public DepartmentDto getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}
	
	public StaffDepartmentTaskRoleDto() {
		
	}
	public StaffDepartmentTaskRoleDto(StaffDepartmentTaskRole entity) {
		staff = new StaffDto(entity.getStaff());
		role = new TaskRoleDto(entity.getRole());
		department = new DepartmentDto(entity.getDepartment());
	}
}
