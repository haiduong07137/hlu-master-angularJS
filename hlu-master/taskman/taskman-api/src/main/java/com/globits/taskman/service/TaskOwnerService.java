package com.globits.taskman.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;

import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.service.GenericService;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.dto.TaskOwnerDto;

public interface TaskOwnerService extends GenericService<TaskOwner, Long> {
	public TaskOwnerDto getTaskOwner(Long id);
	public TaskOwnerDto saveTaskOwner(TaskOwnerDto dto);
	public Boolean deleteTaskOwner(Long id);
	boolean deleteMultipleTaskOwner(TaskOwnerDto[] dtos);
	public TaskOwner setValue(TaskOwnerDto dto, TaskOwner entity, String userName, LocalDateTime currentDate);
	TaskOwnerDto saveTaskOwnerFromPerson(PersonDto dto);
	TaskOwnerDto saveTaskOwnerFromDepartment(DepartmentDto dto);
	List<TaskOwnerDto> getListTaskOwnerDtoByUserAndRole(Long userId, Long roleId);
	List<TaskOwnerDto> getListTaskOwnerDtoByRole(Long roleId);
	List<TaskOwnerDto> getListCurrentTaskOwner();
	List<TaskOwnerDto> getListTaskOwnerByRoleCode(String roleCode);
	TaskOwnerDto getOneTaskOwnerFromPerson(Long personId);
	TaskOwnerDto getOneTaskOwnerFromDepartment(Long departmentId);
	
	public Page<PersonDto> getListPerson(int pageSize, int pageIndex);
	
	List<TaskOwnerDto> getListTaskOwnerFromPerson(Long personId);
	public Page<TaskOwnerDto> getListTaskOwner(int pageSize, int pageIndex);
	Page<TaskOwnerDto> searchTaskOwnerByName(int pageSize, int pageIndex, String text);
	public Page<TaskOwnerDto> searchTaskOwnerByPersonName(int pageSize, int pageIndex, String text);

	public Page<TaskOwnerDto> searchTaskOwnerByNameAndProjectId(long projectId, int pageSize, int pageIndex,
			String text);
	public Page<TaskOwnerDto> searchTaskOwnerByProjectId(long projectId, int pageSize, int pageIndex);
	public Page<TaskOwnerDto> getListTreeGrid(int pageSize, int pageIndex);
	public Page<TaskOwnerDto> getTaskOwnerByTextSearchExcludingTaskOwner(String text, Long taskOwnerId, int pageIndex, int pageSize);
}
