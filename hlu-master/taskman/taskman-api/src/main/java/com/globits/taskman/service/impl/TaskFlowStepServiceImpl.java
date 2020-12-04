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
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.dto.TaskFlowStepDto;
import com.globits.taskman.repository.TaskFlowStepRepository;
import com.globits.taskman.service.TaskFlowStepService;
@Service
@Transactional
public class TaskFlowStepServiceImpl extends GenericServiceImpl<TaskFlowStep, Long> implements TaskFlowStepService{
	@Autowired
	TaskFlowStepRepository repository;
		
	private TaskFlowStep setValue(TaskFlowStepDto dto, TaskFlowStep entity) {
		if(dto !=null) {
			entity.setIsMandatory(dto.getIsMandatory());
			entity.setOrderIndex(dto.getOrderIndex());
		}
//		if(entity.getStep()!=null) {
//			TaskStep taskStep = dto.getStep();
//			taskStep.setId(entity.getStep().getId());
//			taskStep.setName(entity.getStep().getName());
//			taskStep.setCode(entity.getStep().getCode());
//			entity.setStep(taskStep);
//		}
//		if(dto.getFlow()!=null) {
//			
//		}
		return entity;
	}
	@Override
	public TaskFlowStepDto getTaskFlowStep(Long id) {
		TaskFlowStep docCategory = repository.findOne(id);
		return new TaskFlowStepDto(docCategory);
	}

	@Override
	public TaskFlowStepDto saveTaskFlowStep(TaskFlowStepDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		
		 User modifiedUser = null;
		  LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserFlow = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserFlow = modifiedUser.getUsername();
		 }
		 if(dto!=null) {
			TaskFlowStep entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new TaskFlowStep();
				entity.setCreatedBy(currentUserFlow);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity);
			entity = repository.save(entity);
			return new TaskFlowStepDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskFlowStep(Long id) {
		TaskFlowStep doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskFlowStepDto> getListTaskFlowStep(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskFlowStep(pageable);
	}
}
