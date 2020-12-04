package com.globits.taskman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.dto.TaskOwnerDto;
@Repository
public interface TaskOwnerRepository  extends JpaRepository<TaskOwner, Long> {
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(w) from TaskOwner w")
	public Page<TaskOwnerDto> getListTaskOwnerDto(Pageable pageable);

	@Query("select w from TaskOwner w left join fetch w.userTaskOwners u where u.user.id=?1")
	public List<TaskOwner> getListTaskOwnerDtoByUser(Long userId);

	@Query("select new com.globits.taskman.dto.TaskOwnerDto(taskOwner) from UserTaskOwner w where w.user.id=?1 and w.role.id=?2")
	public List<TaskOwnerDto> getListTaskOwnerDtoByUserAndRole(Long userId, Long roleId);

	
	@Query("from TaskOwner w where w.person.id=?1")
	public List<TaskOwner> getListTaskOwnerByPerson(Long personId);
	
	@Query("from TaskOwner w where w.department.id=?1")
	public List<TaskOwner> getListTaskOwnerByDepartment(Long departmentId);
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(taskOwner) from UserTaskOwner w where w.role.code=?1")
	public List<TaskOwnerDto> getListTaskOwnerByRoleCode(String roleCode);
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(w) from TaskOwner w where w.person.id=?1")
	public List<TaskOwnerDto> getTaskOwnerFromPerson(Long personId);	
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d")
	public Page<TaskOwnerDto> getListTaskOwner(Pageable pageable);	
	
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d where d.person.displayName like %?1%")
	public Page<TaskOwnerDto> searchTaskOwnerByPersonName(String text, Pageable pageable);
	
	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d where d.displayName like %?1%")
	public Page<TaskOwnerDto> searchTaskOwnerByName(String text, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d INNER JOIN ProjectTaskOwner p ON d.id = p.taskOwner.id where p.project.id =?1 and p.taskOwner.displayName like %?2% ")
	public Page<TaskOwnerDto> searchTaskOwnerByNameAndProjectId(long projectId, String text, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d INNER JOIN ProjectTaskOwner p ON d.id = p.taskOwner.id where p.project.id =?1 ")
	public Page<TaskOwnerDto> searchTaskOwnerByProjectId(long projectId, Pageable pageable);

	@Query("select new com.globits.taskman.dto.TaskOwnerDto(d) from TaskOwner d where d.parent is null")
	public Page<TaskOwnerDto> getListTreeGrid(Pageable pageable);
}
