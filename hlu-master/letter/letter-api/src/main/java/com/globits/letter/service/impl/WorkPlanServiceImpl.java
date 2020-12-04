package com.globits.letter.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.globits.calendar.domain.Event;
import com.globits.calendar.dto.EventDto;
import com.globits.core.utils.CommonUtils;
import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.service.WorkPlanService;
import com.globits.security.domain.User;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskFileAttachment;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.repository.TaskFileAttachmentRepository;
import com.globits.taskman.repository.TaskRepository;
import com.globits.taskman.service.TaskService;
import com.globits.taskman.service.UserTaskOwnerService;
@Service
public class WorkPlanServiceImpl implements WorkPlanService{
	
	@Autowired
	TaskService taskService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskFileAttachmentRepository taskFileAttachmentRepository;
	@Autowired
	UserTaskOwnerService userTaskOwnerService; 
	@Override
	public TaskDto saveWorkPlan(TaskDto task) {
		if(task.getFlow()==null) {
			task.setFlow(new TaskFlowDto());
		}
		task.getFlow().setId(LetterConstant.WorkPlanFlow.getId());
		task = taskService.saveTask(task);
		return task;
	}
	
	@Override
	public Page<TaskDto> getListWorkPlan(int pageIndex, int pageSize) {
		return taskService.getListTaskByFlow(LetterConstant.WorkPlanFlow.getId(), pageSize, pageIndex);
	}

	@Override
	public boolean deleteMultiple(TaskDto[] dtos) {

		if (CommonUtils.isEmpty(dtos)) {
			return false;
		}

		for (TaskDto d : dtos) {
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid task ID found!");
			}

			Task task = taskRepository.findOne(d.getId());
			if (task != null) {
				if (task.getAttachments() != null && task.getAttachments().size() > 0) {
					for (TaskFileAttachment file : task.getAttachments()) {
						if (file.getId() != null) {
							taskFileAttachmentRepository.delete(file.getId());
						}
					}
				}
				taskRepository.delete(task);
			}
		}

		return true;
	}

	@Override
	public TaskDto getWorkPlansById(Long id) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		
		 User modifiedUser = null;
		  LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		Task entity = taskRepository.getFullTaskById(id);
		if(entity!=null) {
			TaskDto ret = new TaskDto(entity);
			List<UserTaskOwner> utos= userTaskOwnerService.getListTaskUserByOwnerId(modifiedUser.getId());
			for(ParticipateDto p:ret.getParticipates()) {
				if(p.getTaskOwner()!=null) {
					for(UserTaskOwner uto:utos) {
						Boolean hasOwnerPermission = uto.getTaskOwner()!=null && (uto.getTaskOwner().getId()==p.getTaskOwner().getId());
						p.setHasOwnerPermission(hasOwnerPermission);
						if(hasOwnerPermission) {
							ret.setHasCommentPermission(true);//Đặt quyền được phép comment cho cả task này.
						}
					}
				}
			}
			return ret;
		}
		return null;
	}

	@Override
	public Page<TaskDto> getListWorkPlanByStep(Long stepId, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return taskService.getListTaskByFlowAndStepId(LetterConstant.WorkPlanFlow.getId(),stepId, pageSize, pageIndex);
	}

	@Override
	public Boolean addComment(TaskCommentDto comment) {
		Boolean result = taskService.addComment(comment);
		return result;
	}
}
