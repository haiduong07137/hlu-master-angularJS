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
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.repository.TaskStepRepository;
import com.globits.taskman.service.TaskStepService;
@Service
@Transactional
public class TaskStepServiceImpl extends GenericServiceImpl<TaskStep, Long> implements TaskStepService{
	@Autowired
	TaskStepRepository repository;
		
	private TaskStep setValue(TaskStepDto dto, TaskStep entity) {
		if(dto !=null) {
			entity.setCode(dto.getCode());
			entity.setName(dto.getName());
		}
		return entity;
	}
	@Override
	public TaskStepDto getTaskStep(Long id) {
		TaskStep docCategory = repository.findOne(id);
		return new TaskStepDto(docCategory);
	}

	@Override
	public TaskStepDto saveTaskStep(TaskStepDto dto) {
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
			TaskStep entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new TaskStep();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity);
			entity = repository.save(entity);
			return new TaskStepDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskStep(Long id) {
		TaskStep doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskStepDto> getListTaskStep(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskStep(pageable);
	}
	@Override
	public TaskStepDto getTaskStepByCode(String code) {
		return repository.getTaskStepByCode(code);
	}
	@Override
	public TaskStep getTaskStepEntityByCode(String code) {
		// TODO Auto-generated method stub
		return repository.getTaskStepEntityByCode(code);
	}
}
