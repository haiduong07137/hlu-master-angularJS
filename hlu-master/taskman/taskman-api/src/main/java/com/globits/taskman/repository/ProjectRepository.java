package com.globits.taskman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.Project;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.TaskRoleDto;
@Repository
public interface ProjectRepository  extends JpaRepository<Project, Long> {
	@Query("select new com.globits.taskman.dto.ProjectDto(p) from Project p")
	public Page<ProjectDto> getListProject(Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.ProjectDto(p) from Project p where p.code =?1")
	List<ProjectDto> getProjectByCode(String code);
	
	@Query("from Project p where p.code =?1")
	List<Project> getProjectEntityByCode(String code);
	
	@Query("select new com.globits.taskman.dto.ProjectDto(p) from Project p where p.code like %?1% or p.name like %?1%")
	public Page<ProjectDto> findPageByCodeOrName(String code, Pageable pageable);
}
