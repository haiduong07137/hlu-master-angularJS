package com.globits.taskman.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.security.domain.User;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.service.TaskRoleService;
@Service
@Transactional
public class TaskRoleServiceImpl extends GenericServiceImpl<TaskRole, Long> implements TaskRoleService{
	@Autowired
	TaskRoleRepository repository;
		
	private TaskRole setValue(TaskRoleDto dto, TaskRole entity) {
		if(dto !=null) {
			entity.setCode(dto.getCode());
			entity.setName(dto.getName());
		}
		return entity;
	}
	@Override
	public TaskRoleDto getTaskRole(Long id) {
		TaskRole docCategory = repository.findOne(id);
		return new TaskRoleDto(docCategory);
	}

	@Override
	public TaskRoleDto saveTaskRole(TaskRoleDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		
		 User modifiedUser = null;
		  LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 if(dto!=null) {
			 TaskRole entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new TaskRole();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity);
			entity = repository.save(entity);
			return new TaskRoleDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskRole(Long id) {
		TaskRole doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskRoleDto> getListTaskRole(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskRole(pageable);
	}

	@Override
	public List<TaskRoleDto> getTaskRoleByCodes(String code) {
		return repository.getTaskRoleByCode(code);
	}

	@Override
	public TaskRoleDto getTaskRoleByCode(String code) {
		List<TaskRoleDto> taskRoles = repository.getTaskRoleByCode(code);
		if(taskRoles!=null && taskRoles.size()>0) {
			return taskRoles.get(0);
		}
		return null;
	}
	@Override
	public TaskRole getTaskRoleEntityByCode(String code) {
		List<TaskRole> taskRoles = repository.getTaskRoleEntityByCode(code);
		if(taskRoles!=null && taskRoles.size()>0) {
			return taskRoles.get(0);
		}
		return null;
	}
}
