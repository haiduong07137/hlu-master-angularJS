package com.globits.taskman.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

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
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;
import com.globits.security.domain.User;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.repository.ParticipateRepository;
import com.globits.taskman.repository.TaskRepository;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.service.ParticipateService;
@Service
@Transactional
public class ParticipateServiceImpl extends GenericServiceImpl<Participate, Long> implements ParticipateService{
	@Autowired
	ParticipateRepository repository;
	@Autowired
	StaffRepository staffRepository;
	
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	TaskRoleRepository taskRoleRepository;
	@Override
	public Participate setValue(ParticipateDto dto, Participate entity, String userName, LocalDateTime currentDate) {
		if(dto !=null) {
			//Xử lý các trường đơn trước 
			if(dto.getId()!=null) {
				entity.setId(dto.getId());//trường hợp cập nhật dữ liệu
			}
			entity.setDisplayName(dto.getDisplayName());
			entity.setParticipateType(dto.getParticipateType());
			//Nếu participate này đã có 1 số comment và được đẩy lên
			if(dto.getComments()!=null) {
				if(entity.getComments()==null) {//Nếu chưa có comment nào thì thêm 1 HashSet vào
					entity.setComments(new HashSet<TaskComment>());	
				}else {//nếu không thì thì clear (xóa hết comment cũ) đi để thêm mới
					entity.getComments().clear();
				}
				
				for(TaskCommentDto commentDto:dto.getComments()) {
					TaskComment comment = new TaskComment();
					comment.setComment(commentDto.getComment());
					comment.setId(commentDto.getId());
					comment.setParticipate(entity);
					entity.getComments().add(comment);//thêm comment vào
				}
			}
			Staff employee = null;
			if(dto.getEmployee()!=null) {
				if(dto.getEmployee().getId()!=null) {
					employee = staffRepository.findById(dto.getEmployee().getId());
				}
			}
			entity.setEmployee(employee);
			Task task = null;
			if(dto.getTask()!=null) {
				if(dto.getTask().getId()!=null) {
					task = taskRepository.findOne(dto.getId());
				}
			}
			
			entity.setTask(task);
			TaskRole role =null;
			if(dto.getRole()!=null) {
				if(dto.getRole().getId()!=null)
					role = taskRoleRepository.getOne(dto.getRole().getId());
			}
			entity.setRole(role);
		}
		return entity;
	}
	@Override
	public ParticipateDto getParticipate(Long id) {
		Participate docCategory = repository.findOne(id);
		return new ParticipateDto(docCategory);
	}

	@Override
	public ParticipateDto saveParticipate(ParticipateDto dto) {
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
			Participate entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new Participate();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity, currentUserName,currentDate);
			entity = repository.save(entity);
			return new ParticipateDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteParticipate(Long id) {
		Participate doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<ParticipateDto> getListParticipate(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListParticipate(pageable);
	}
	@Override
	public List<Participate> getTaskParticipate(Long taskId, Long ownerId, Long roleId, Integer participateType) {
		
		String sql ="from Participate p where p.task.id=:taskId and p.role.id=:roleId and p.employee.id=:ownerId and p.participateType=:participateType";
		if(participateType == TaskManConstant.ParticipateTypeEnum.DepartmentType.getValue()) {
			sql ="from Participate p where p.task.id=:taskId and p.role.id=:roleId and p.department.id=:ownerId and p.participateType=:participateType";
		}
		Query q = manager.createQuery(sql,Participate.class);
		q.setParameter("taskId", taskId);
		q.setParameter("roleId", roleId);
		q.setParameter("participateType", participateType);
		q.setParameter("ownerId", ownerId);
		return q.getResultList();
	}
	@Override
	public Participate getTaskParticipateBy(Long taskId, Long taskownerId, Long roleId) {

		String sql ="from Participate p where p.task.id=:taskId and p.role.id=:roleId and p.taskOwner.id=:taskownerId ";
		Query q = manager.createQuery(sql,Participate.class);
		q.setParameter("taskId", taskId);
		q.setParameter("roleId", roleId);
		q.setParameter("taskownerId", taskownerId);
		if (q.getResultList().size() > 0) {
			return (Participate) q.getResultList().get(0);
		}
		else {
			return null;
		}
	}
}
