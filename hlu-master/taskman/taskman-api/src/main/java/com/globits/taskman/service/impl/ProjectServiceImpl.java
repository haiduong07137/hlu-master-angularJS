package com.globits.taskman.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

import com.globits.core.domain.FileDescription;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.security.domain.User;
import com.globits.taskman.domain.Project;
import com.globits.taskman.domain.ProjectFileAttachment;
import com.globits.taskman.domain.ProjectTaskOwner;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskFileAttachment;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.ProjectFileAttachmentDto;
import com.globits.taskman.dto.ProjectTaskOwnerDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.repository.ProjectFileAttachmentRepository;
import com.globits.taskman.repository.ProjectRepository;
import com.globits.taskman.repository.ProjectTaskOwnerRepository;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.service.ProjectService;
import com.globits.taskman.service.TaskOwnerService;
@Service
@Transactional
public class ProjectServiceImpl extends GenericServiceImpl<Project, Long> implements ProjectService{
	@Autowired
	ProjectRepository repository;
	
	@Autowired
	ProjectTaskOwnerRepository projectTaskOwnerRepository;
	
	@Autowired
	TaskRoleRepository taskRoleRepository;
	
	@Autowired
	TaskOwnerService taskOwnerService;
	
	@Autowired
	ProjectFileAttachmentRepository projectFileAttachmentRepository;

	@Autowired
	FileDescriptionRepository fileDescriptionRepository;
	
	private Project setValue(ProjectDto dto, Project entity) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		
		 User modifiedUser = null;
		  LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		if(dto !=null) {
			entity.setCode(dto.getCode());
			entity.setName(dto.getName());
			entity.setDescription(dto.getDescription());
		}
		
		List<ProjectTaskOwner> members = new ArrayList<ProjectTaskOwner>();
		if(dto.getMembers()!=null) {
			
			for(ProjectTaskOwnerDto ptoDto:dto.getMembers()) {
				ProjectTaskOwner pto = null;
				if (ptoDto.getId() != null) {
					pto = projectTaskOwnerRepository.findOne(ptoDto.getId());
				}
				if(pto==null) {
					pto = new ProjectTaskOwner();
					pto.setCreateDate(currentDate);
					pto.setCreatedBy(currentUserName);
				}
				if (dto != null) {
					pto.setProject(entity);
				}
				if(ptoDto.getTaskOwner()!=null && ptoDto.getTaskOwner().getId()!=null) {
					TaskOwner  taskOwner = taskOwnerService.findById(ptoDto.getTaskOwner().getId());
					pto.setTaskOwner(taskOwner);			
				}

				if(ptoDto.getMainRole()!=null) {
					
					TaskRole mainRole=null;
					if(ptoDto.getMainRole().getId()!=null)
						mainRole = taskRoleRepository.findOne(ptoDto.getMainRole().getId());
					if(mainRole==null && ptoDto.getMainRole().getCode()!=null) {
						List<TaskRole> listMainRoles = taskRoleRepository.getTaskRoleEntityByCode(ptoDto.getMainRole().getCode());
						if(listMainRoles!=null && listMainRoles.size()>0) {
							mainRole = listMainRoles.get(0);
						}
					}
					pto.setMainRole(mainRole);
				}
				members.add(pto);
			}
			
			if(entity.getMembers()==null) {
				entity.setMembers(new HashSet<ProjectTaskOwner>());
			}
			entity.getMembers().clear();
			entity.getMembers().addAll(members);
		}

		if (entity.getAttachments() != null && entity.getAttachments().size() > 0) {
			entity.getAttachments().clear();
		}
		if(dto.getAttachments()!=null && dto.getAttachments().size()>0) {
			for(ProjectFileAttachmentDto aDto:dto.getAttachments()) {
				ProjectFileAttachment attachment = null;
				if(aDto.getId()!=null) {
					attachment = projectFileAttachmentRepository.findOne(aDto.getId());	
				}
				if(attachment==null) {
					attachment = new ProjectFileAttachment();
					attachment.setCreateDate(currentDate);
					attachment.setCreatedBy(currentUserName);
				}
				attachment.setProject(entity);
				FileDescription file = null;
				if(aDto.getFile()!=null && aDto.getFile().getId()!=null) {
					file = fileDescriptionRepository.findOne(aDto.getFile().getId());
				}
				attachment.setFile(file);
				attachment = projectFileAttachmentRepository.save(attachment);//Cần dòng này để đảm bảo không bị lỗi detach - chưa hiểu sao
				entity.getAttachments().add(attachment);
			}
		}
		
		return entity;
	}
	@Override
	public ProjectDto getProject(Long id) {
		Project project = repository.findOne(id);
		return new ProjectDto(project);
	}

	@Override
	public ProjectDto saveProject(ProjectDto dto) {
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
			 Project entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new Project();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity);
			entity = repository.save(entity);
			return new ProjectDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteProject(Long id) {
		Project project = repository.findOne(id);
		if(project!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}


	
	@Override
	public Page<ProjectDto> geListProjectPageByUserId(Long userId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		String query = "select new com.globits.taskman.dto.ProjectDto(pto.project) from ProjectTaskOwner pto where pto.taskOwner.id in (select uto.taskOnwer.id from UserTaskOwner uto where uto.user.id=:userId)";
		
		String queryCount ="select count(pto.project.id) from ProjectTaskOwner pto where pto.taskOwner.id in (select uto.taskOnwer.id from UserTaskOwner uto where uto.user.id=:userId)";

		Query q = manager.createQuery(query, Task.class);
		Query qCount = manager.createQuery(queryCount);
		
		q.setParameter("userId", userId);
		qCount.setParameter("userId", userId);
		
		q.setMaxResults(pageSize);
		int startPosition = (pageIndex-1)*pageSize;
		q.setFirstResult(startPosition);
		List<ProjectDto> content =  q.getResultList();
		Long total =(Long)qCount.getSingleResult();
		Page<ProjectDto> result = new PageImpl<ProjectDto>(content,pageable,total);
		
		return result;
	}
	
	@Override
	public Page<ProjectDto> getListProject(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListProject(pageable);
	}
	@Override
	public List<ProjectDto> getProjectByCode(String code) {
		// TODO Auto-generated method stub
		return repository.getProjectByCode(code);
	}
	
	@Override
	public List<ProjectDto> getMyListProject() {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		
		 User modifiedUser = null;
		  LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 return geListProjectByUserId(modifiedUser.getId());
	}
	
	@Override
	public List<ProjectDto> geListProjectByUserId(Long userId) {
		String query = "select new com.globits.taskman.dto.ProjectDto(p) from Project p where p.id in (select pto.project.id from ProjectTaskOwner pto where pto.taskOwner.id in (select uto.taskOwner.id from UserTaskOwner uto where uto.user.id=:userId))";

		Query q = manager.createQuery(query, ProjectDto.class);
		q.setParameter("userId", userId);
		List<ProjectDto> content =  q.getResultList();
		return content;
	}
	
	@Override
	public Page<ProjectDto> findPageByCodeOrName(String textSearch, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.findPageByCodeOrName(textSearch, pageable);
	}}
