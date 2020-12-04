package com.globits.taskman.service.impl;

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
import com.globits.taskman.domain.TaskPriority;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.TaskPriorityDto;
import com.globits.taskman.repository.TaskPriorityRepository;
import com.globits.taskman.service.TaskPriorityService;
@Service
@Transactional
public class TaskPriorityServiceImpl extends GenericServiceImpl<TaskPriority, Long> implements TaskPriorityService{
	@Autowired
	TaskPriorityRepository repository;
		
	private TaskPriority setValue(TaskPriorityDto dto, TaskPriority entity) {
		if(dto !=null) {
			entity.setCode(dto.getCode());
			entity.setName(dto.getName());
		}
		return entity;
	}
	@Override
	public TaskPriorityDto getTaskPriority(Long id) {
		TaskPriority docCategory = repository.findOne(id);
		return new TaskPriorityDto(docCategory);
	}

	@Override
	public TaskPriorityDto saveTaskPriority(TaskPriorityDto dto) {
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
			TaskPriority entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new TaskPriority();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity);
			entity = repository.save(entity);
			return new TaskPriorityDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskPriority(Long id) {
		TaskPriority doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskPriorityDto> getListTaskPriority(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize );
		return repository.getListTaskPriority(pageable);
	}
	
}
