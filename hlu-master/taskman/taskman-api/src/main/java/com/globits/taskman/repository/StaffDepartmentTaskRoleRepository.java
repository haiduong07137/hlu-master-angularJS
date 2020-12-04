package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.StaffDepartmentTaskRole;
import com.globits.taskman.dto.StaffDepartmentTaskRoleDto;
@Repository
public interface StaffDepartmentTaskRoleRepository  extends JpaRepository<StaffDepartmentTaskRole, Long> {
	@Query("select new com.globits.taskman.dto.StaffDepartmentTaskRoleDto(d) from StaffDepartmentTaskRole d")
	public Page<StaffDepartmentTaskRoleDto> getListStaffDepartmentTaskRole(Pageable pageable);
}

