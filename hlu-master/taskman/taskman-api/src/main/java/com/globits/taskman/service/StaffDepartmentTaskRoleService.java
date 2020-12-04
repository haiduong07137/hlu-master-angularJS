package com.globits.taskman.service;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.StaffDepartmentTaskRole;
import com.globits.taskman.dto.StaffDepartmentTaskRoleDto;

public interface StaffDepartmentTaskRoleService extends GenericService<StaffDepartmentTaskRole, Long> {
	public StaffDepartmentTaskRoleDto getStaffDepartmentTaskRole(Long id);
	public StaffDepartmentTaskRoleDto saveStaffDepartmentTaskRole(StaffDepartmentTaskRoleDto dto);
	public Boolean deleteStaffDepartmentTaskRole(Long id);
	public Page<StaffDepartmentTaskRoleDto> getListStaffDepartmentTaskRole(int pageSize, int pageIndex);
	public StaffDepartmentTaskRole setValue(StaffDepartmentTaskRoleDto dto, StaffDepartmentTaskRole entity, String userName, LocalDateTime currentDate);
}
