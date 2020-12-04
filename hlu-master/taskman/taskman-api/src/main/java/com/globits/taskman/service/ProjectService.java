package com.globits.taskman.service;

import java.util.List;

import org.springframework.data.domain.Page;
import com.globits.core.service.GenericService;
import com.globits.taskman.domain.Project;
import com.globits.taskman.dto.ProjectDto;

public interface ProjectService extends GenericService<Project, Long> {
	public List<ProjectDto> getProjectByCode(String code);

	public ProjectDto getProject(Long id);

	public ProjectDto saveProject(ProjectDto dto);

	public Boolean deleteProject(Long id);

	public Page<ProjectDto> getListProject(int pageSize, int pageIndex);
	Page<ProjectDto> geListProjectPageByUserId(Long userId, int pageSize, int pageIndex);

	List<ProjectDto> geListProjectByUserId(Long userId);

	List<ProjectDto> getMyListProject();
	
	public Page<ProjectDto> findPageByCodeOrName(String textSearch, int pageIndex, int pageSize);
}
