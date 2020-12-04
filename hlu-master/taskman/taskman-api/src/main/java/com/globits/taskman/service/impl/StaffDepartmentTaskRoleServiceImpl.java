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

import com.globits.core.domain.Department;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;
import com.globits.security.domain.User;
import com.globits.taskman.domain.StaffDepartmentTaskRole;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.StaffDepartmentTaskRoleDto;
import com.globits.taskman.repository.StaffDepartmentTaskRoleRepository;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.service.StaffDepartmentTaskRoleService;
@Service
@Transactional
public class StaffDepartmentTaskRoleServiceImpl extends GenericServiceImpl<StaffDepartmentTaskRole, Long> implements StaffDepartmentTaskRoleService{
	@Autowired
	StaffDepartmentTaskRoleRepository repository;
	@Autowired
	StaffRepository staffRepository;
	
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	TaskRoleRepository taskRoleRepository;
	@Override
	public StaffDepartmentTaskRole setValue(StaffDepartmentTaskRoleDto dto, StaffDepartmentTaskRole entity, String userName, LocalDateTime currentDate) {
		if(dto !=null) {
			//Xử lý các trường đơn trước 
			if(dto.getId()!=null) {
				entity.setId(dto.getId());//trường hợp cập nhật dữ liệu
			}

			Staff staff = null;
			if(dto.getStaff()!=null) {
				if(dto.getStaff().getId()!=null) {
					staff = staffRepository.findById(dto.getStaff().getId());
				}
			}
			entity.setStaff(staff);
			Department department=null; 
			if(dto.getDepartment()!=null) {
				if(dto.getDepartment().getId()!=null) {
					department = departmentRepository.findById(dto.getDepartment().getId());
				}
			}
			entity.setDepartment(department);
			
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
	public StaffDepartmentTaskRoleDto getStaffDepartmentTaskRole(Long id) {
		StaffDepartmentTaskRole docCategory = repository.findOne(id);
		return new StaffDepartmentTaskRoleDto(docCategory);
	}

	@Override
	public StaffDepartmentTaskRoleDto saveStaffDepartmentTaskRole(StaffDepartmentTaskRoleDto dto) {
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
			StaffDepartmentTaskRole entity = null;
			if(dto.getId()!=null) {//Có khả năng là update
				entity = repository.findOne(dto.getId());//Lấy dữ liệu từ database ra.
			}
			if(entity==null) {//trường hợp thêm mới
				entity = new StaffDepartmentTaskRole();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto,entity, currentUserName,currentDate);
			entity = repository.save(entity);
			return new StaffDepartmentTaskRoleDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteStaffDepartmentTaskRole(Long id) {
		StaffDepartmentTaskRole doc = repository.findOne(id);
		if(doc!=null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<StaffDepartmentTaskRoleDto> getListStaffDepartmentTaskRole(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListStaffDepartmentTaskRole(pageable);
	}
}
