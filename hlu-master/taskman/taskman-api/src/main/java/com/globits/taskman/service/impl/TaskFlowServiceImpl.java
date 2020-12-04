package com.globits.taskman.service.impl;

import java.util.HashSet;
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
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskFlowStepDto;
import com.globits.taskman.repository.TaskFlowRepository;
import com.globits.taskman.repository.TaskFlowStepRepository;
import com.globits.taskman.repository.TaskStepRepository;
import com.globits.taskman.service.TaskFlowService;
@Service
@Transactional
public class TaskFlowServiceImpl extends GenericServiceImpl<TaskFlow, Long> implements TaskFlowService{
	@Autowired
	TaskFlowRepository repository;
	@Autowired
	TaskFlowStepRepository taskFlowStepRepository;
	@Autowired
	TaskStepRepository taskStepRepository;
	private TaskFlow setValue(TaskFlowDto dto, TaskFlow entity,String userName, LocalDateTime currentDate) {
		if(dto !=null) {
			entity.setCode(dto.getCode());
			entity.setName(dto.getName());
		}
		if(dto.getSteps()!=null && dto.getSteps().size()>0) {
			if(entity.getSteps()==null) {
				entity.setSteps(new HashSet<TaskFlowStep>());
			}else {
				entity.getSteps().clear();
			}
			for(TaskFlowStepDto stepDto : dto.getSteps()) {
				TaskFlowStep flowStep = null;
				if(stepDto.getId()!=null)
					flowStep = taskFlowStepRepository.findOne(stepDto.getId());
				if(flowStep==null) {
					flowStep = new TaskFlowStep();
					flowStep.setFlow(entity);
					flowStep.setCreateDate(currentDate);
					flowStep.setCreatedBy(userName);
				}
				TaskStep step=null;
				if(stepDto.getStep().getId()!=null)
					step = taskStepRepository.findOne(stepDto.getStep().getId());
				if(step==null) {
					step = new TaskStep();
					step.setCreateDate(currentDate);
					step.setCreatedBy(userName);
				}
				step.setCode(stepDto.getStep().getCode());
				step.setName(stepDto.getStep().getName());
				flowStep.setStep(step);
				flowStep.setOrderIndex(stepDto.getOrderIndex());
				flowStep = taskFlowStepRepository.save(flowStep);
				entity.getSteps().add(flowStep);
			}
		}
		return entity;
	}
	@Override
	public TaskFlowDto getTaskFlow(Long id) {
		TaskFlow docCategory = repository.findOne(id);
		return new TaskFlowDto(docCategory);
	}

	@Override
	public TaskFlowDto saveTaskFlow(TaskFlowDto dto) {
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
			TaskFlow entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new TaskFlow();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity,currentUserName,currentDate);
			entity = repository.save(entity);
			return new TaskFlowDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskFlow(Long id) {
		TaskFlow doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskFlowDto> getListTaskFlow(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskFlow(pageable);
	}
	@Override
	public TaskFlowDto getTaskFlowByCode(String code) {
		return repository.getTaskFlowByCode(code);
	}
	@Override
	public TaskFlow getFullTaskFlowById(Long id) {
		TaskFlow taskFlow= repository.getFullTaskFlowById(id);
		return taskFlow;
	}
	@Override
	public List<TaskFlowDto> getAll() {
		List<TaskFlowDto> lstTaskFlow= repository.getAll();
		return lstTaskFlow;
	}
}
