package com.globits.taskman.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskPriority;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.TaskRoleDto;
@Repository
public interface TaskRoleRepository  extends JpaRepository<TaskRole, Long> {
	@Query("select new com.globits.taskman.dto.TaskRoleDto(d) from TaskRole d")
	public Page<TaskRoleDto> getListTaskRole(Pageable pageable);
	@Query("select new com.globits.taskman.dto.TaskRoleDto(r) from TaskRole r where r.code =?1")
	List<TaskRoleDto> getTaskRoleByCode(String code);
	@Query("from TaskRole r where r.code =?1")
	List<TaskRole> getTaskRoleEntityByCode(String code);
}
