package com.globits.taskman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.Project;
import com.globits.taskman.domain.ProjectTaskOwner;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.TaskRoleDto;
@Repository
public interface ProjectTaskOwnerRepository  extends JpaRepository<ProjectTaskOwner, Long> {

}
