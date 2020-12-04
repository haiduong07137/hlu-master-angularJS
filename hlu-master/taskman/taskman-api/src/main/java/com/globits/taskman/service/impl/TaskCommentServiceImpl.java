package com.globits.taskman.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.core.domain.FileDescription;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.security.domain.User;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.dto.CommentFileAttachmentDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.repository.CommentFileAttachmentRepository;
import com.globits.taskman.repository.ParticipateRepository;
import com.globits.taskman.repository.TaskCommentRepository;
import com.globits.taskman.service.TaskCommentService;

@Service
@Transactional
public class TaskCommentServiceImpl extends GenericServiceImpl<TaskComment, Long> implements TaskCommentService {
	@Autowired
	TaskCommentRepository repository;
	@Autowired
	ParticipateRepository participateRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	CommentFileAttachmentRepository commentFileAttachmentRepository;
	@Autowired
	FileDescriptionRepository fileDescriptionRepository;
	private TaskComment setValue(TaskCommentDto dto, TaskComment entity) {
		if (dto != null) {
			if (dto.getParticipate() != null) {
				Participate participate = participateRepository.findOne(dto.getParticipate().getId());
				entity.setParticipate(participate);
			}
			entity.setComment(dto.getComment());
			entity.setFilePath(dto.getFilePath());
//			entity.setAttachments(attachments);
		}
		return entity;
	}

	@Override
	public TaskCommentDto getTaskComment(Long id) {
		TaskComment docCategory = repository.findOne(id);
		return new TaskCommentDto(docCategory);
	}

	@Override
	public TaskCommentDto addEditTaskComment(TaskCommentDto dto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		TaskComment taskComment = null;
		if (dto.getId() != null) {
			taskComment = repository.findOne(dto.getId());
		}
		
		if (taskComment == null) {
			taskComment = new TaskComment();
			taskComment.setCreateDate(currentDate);
			taskComment.setModifyDate(currentDate);
			taskComment.setUserName(currentUserName);
		}
		if (dto.getAttachments() != null && dto.getAttachments().size() > 0) {
			if (taskComment.getAttachments() != null) {
				taskComment.getAttachments().clear();
			} else {
				taskComment.setAttachments(new HashSet<CommentFileAttachment>());
			}
			for (CommentFileAttachmentDto _a : dto.getAttachments()) {

				CommentFileAttachment a = null;
				if (CommonUtils.isPositive(_a.getId(), true)) {
					a = commentFileAttachmentRepository.findOne(_a.getId());
				}

				if (a == null) {
					a = new CommentFileAttachment();
					a.setCreateDate(currentDate);
					a.setCreatedBy(currentUserName);
				} else {
					a.setModifyDate(currentDate);
					a.setModifiedBy(currentUserName);
				}
				a.setTaskComment(taskComment);
				if (_a.getFile() != null) {
					FileDescription file = new FileDescription();
					file.setId(_a.getFile().getId());
					if (_a.getFile().getId() == null) {
						file.setContentSize(_a.getFile().getContentSize());
						file.setContentType(_a.getFile().getContentType());
						file.setName(_a.getFile().getName());
						file.setFilePath(_a.getFile().getFilePath());

						file = fileDescriptionRepository.save(file);
					}
					a.setFile(file);
				}
				taskComment.getAttachments().add(a);
			}
		}else {
			taskComment.getAttachments().clear();
		}
		Participate participate = null;
		if(dto.getParticipate() != null && dto.getParticipate().getId() != null) {
			participate = participateRepository.findOne(dto.getParticipate().getId());	
		}
		if(participate != null) {
			taskComment.setParticipate(participate);
		}
		if(dto.getComment() != null){
			taskComment.setComment(dto.getComment());
		} 
		taskComment = repository.save(taskComment);
		return new TaskCommentDto(taskComment);
	}

	@Override
	public TaskCommentDto saveTaskComment(TaskCommentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		if (dto != null) {
			TaskComment entity = null;
			if (dto.getId() != null) {// Có khả năng là update
				entity = repository.findOne(dto.getId());// Lấy dữ liệu từ database ra.
			}
			if (entity == null) {// trường hợp thêm mới
				entity = new TaskComment();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
				entity.setUserName(currentUserName);
			}
			entity = setValue(dto, entity);
			entity = repository.save(entity);
			return new TaskCommentDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskComment(Long id) {
		TaskComment doc = repository.findOne(id);
		if (doc != null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskCommentDto> getListTaskComment(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskComment(pageable);
	}

	@Override
	public Page<TaskCommentDto> getListTaskCommentByTaskId(Long taskId, int pageSize, int pageIndex) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			Long userId = modifiedUser.getId();

			Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
			int startPosition = (pageIndex - 1) * pageSize;
			

			String sql = "select distinct new com.globits.taskman.dto.TaskCommentDto(d) from TaskComment d where d.participate.task.id = :taskId";
			String sqlCount = "select count(distinct d.id) from TaskComment d where d.participate.task.id = :taskId";

			Query q = manager.createQuery(sql, TaskCommentDto.class);
			Query qCount = manager.createQuery(sqlCount);
			q.setFirstResult(startPosition);
			q.setMaxResults(pageSize);

			q.setParameter("taskId", taskId);
			qCount.setParameter("taskId", taskId);

			List<TaskCommentDto> list = q.getResultList();
			long total = (Long) qCount.getSingleResult();

			Boolean hasOwnerPermission =false;
			for (TaskCommentDto taskCommentDto : list) {
				Boolean hasEditCommentPermission = false;
				if (StringUtils.hasText(taskCommentDto.getUserName())) {
					hasEditCommentPermission = taskCommentDto.getUserName().equals(currentUserName);
				}
				
				taskCommentDto.setHasEditCommentPermission(hasEditCommentPermission);
				if (taskCommentDto.getParticipate() != null) {

					if (taskCommentDto.getParticipate().getTaskOwner() != null && taskCommentDto.getParticipate().getTaskOwner().getUserTaskOwners() != null && taskCommentDto.getParticipate().getTaskOwner().getUserTaskOwners().size() > 0) {
						for ( UserTaskOwnerDto uto : taskCommentDto.getParticipate().getTaskOwner().getUserTaskOwners()) {

							//check quyền comment
							if (!taskCommentDto.getParticipate().getHasOwnerPermission()) {
								Boolean ownerPermission = uto.getUser().getId().equals(userId);
								taskCommentDto.getParticipate().setHasOwnerPermission(hasOwnerPermission || ownerPermission);
							}
						}
					}
				}
			}
			
			PageImpl<TaskCommentDto> page = new PageImpl<TaskCommentDto>(list, pageable, total);
			return page;
			
		}
		/*
		 * Pageable pageable = new PageRequest(pageIndex - 1, pageSize); return
		 * repository.getListTaskCommentByTaskId(taskId,pageable);
		 */

		return null;
	}
}
