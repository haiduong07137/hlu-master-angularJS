package com.globits.hr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.Staff;

@Repository
public interface PositionStaffRepository extends JpaRepository<PositionStaff, Long> {
	@Query("select new PositionStaff(u) from PositionStaff u  where u.current=1 and u.department.id = ?1")
	Page<PositionStaff> findTeacherByDepartment(Long departmentId, Pageable pageable);
	
	@Query("select new PositionStaff(u) from PositionStaff u  where u.staff.id=?1 and u.position.id = ?2 and u.department.id = ?3")
	List<PositionStaff> findBy(Long staffId, Long positionId, Long departmentId);
	
	@Query("select distinct(u.staff) from PositionStaff u where u.department.id in ?1")
	List<Staff> findDistinctStaffByDepartment(List<Long> departmentIds);
}
