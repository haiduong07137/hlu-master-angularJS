package com.globits.taskman.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.TaskRoleDto;

public interface TaskRoleService extends GenericService<TaskRole, Long> {
	public List<TaskRoleDto> getTaskRoleByCodes(String code);
	public TaskRole getTaskRoleEntityByCode(String code);
	public TaskRoleDto getTaskRoleByCode(String code);
	public TaskRoleDto getTaskRole(Long id);
	public TaskRoleDto saveTaskRole(TaskRoleDto dto);
	public Boolean deleteTaskRole(Long id);
	public Page<TaskRoleDto> getListTaskRole(int pageSize, int pageIndex);
}
