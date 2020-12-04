package com.globits.taskman.service.impl;

import java.util.List;

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

import com.globits.core.domain.Department;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;
import com.globits.security.domain.User;
import com.globits.security.repository.UserRepository;
import com.globits.security.service.UserService;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.repository.UserTaskOwnerRepository;
import com.globits.taskman.repository.TaskOwnerRepository;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.service.UserTaskOwnerService;
@Service
@Transactional
public class UserTaskOwnerServiceImpl extends GenericServiceImpl<UserTaskOwner, Long> implements UserTaskOwnerService{
	@Autowired
	UserTaskOwnerRepository repository;
	@Autowired
	TaskOwnerRepository taskOwnerRepository;
	
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	TaskRoleRepository taskRoleRepository;
	@Autowired
	UserRepository userRepository;
	@Override
	public UserTaskOwner setValue(UserTaskOwnerDto dto, UserTaskOwner entity, String userName, LocalDateTime currentDate) {
		if(dto !=null) {
			//Xử lý các trường đơn trước 
			if(dto.getId()!=null) {
				entity.setId(dto.getId());//trường hợp cập nhật dữ liệu
			}
			if(dto.getTaskOwner()!=null) {
				TaskOwner taskOwner = taskOwnerRepository.findOne(dto.getTaskOwner().getId());
				entity.setTaskOwner(taskOwner);
			}
			if(dto.getUser()!=null) {
				User user = userRepository.findById(dto.getUser().getId());
				entity.setUser(user);
			}
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
	public UserTaskOwnerDto getUserTaskOwner(Long id) {
		UserTaskOwner docCategory = repository.findOne(id);
		return new UserTaskOwnerDto(docCategory);
	}

	@Override
	public UserTaskOwnerDto saveUserTaskOwner(UserTaskOwnerDto dto) {
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
			UserTaskOwner entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new UserTaskOwner();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity, currentUserName,currentDate);
			entity = repository.save(entity);
			return new UserTaskOwnerDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteUserTaskOwner(Long id) {
		UserTaskOwner doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<UserTaskOwnerDto> getListUserTaskOwner(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListUserTaskOwner(pageable);
	}
	@Override
	public List<UserTaskOwner> getListTaskUserByOwnerId(Long userId) {
		return repository.getListTaskUserByOwnerId(userId);
	}
	@Override
	public Boolean checkUserHasTaskRoleByUserName(String uname, String roleCode) {
		if(uname != null && roleCode != null) {
			String sql = "select new com.globits.taskman.dto.UserTaskOwnerDto(d) from UserTaskOwner d where d.user.username=:uname and d.role.code=:roleCode";
			Query sqlTaskOwner = manager.createQuery(sql,UserTaskOwnerDto.class);
			sqlTaskOwner.setParameter("uname", uname);
			sqlTaskOwner.setParameter("roleCode", roleCode);
			List<UserTaskOwnerDto> userTaskOwnerDtos = sqlTaskOwner.getResultList();
			if(userTaskOwnerDtos != null && userTaskOwnerDtos.size() > 0) {
				return true;
			}
			return false;
			
		}
		
		return null;
	}
}
