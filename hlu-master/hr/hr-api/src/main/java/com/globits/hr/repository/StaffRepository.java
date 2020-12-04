package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffDto;
import com.globits.security.domain.User;

@Repository
public interface StaffRepository
		extends JpaRepository<Staff, Long> { // extends
																									// JpaRepository<Staff,
																									// Long> {
	@Query("select new com.globits.hr.dto.StaffDto(s.id, s.staffCode, s.displayName, s.gender) from Staff s")
	public Page<StaffDto> findByPageBasicInfo(Pageable pageable);

	@Query("select u from Staff u left join fetch u.user where u.id = ?1")
	public Staff findById(Long id);

	@Query("select u from User u left join fetch u.roles where u.username = ?1")
	public User findByUsername(String username);

	@Query("select u from Staff u where u.staffCode = ?1")
	public Staff findByCode(String staffCode);
	
	@Query("select new com.globits.hr.dto.StaffDto(s) from Staff s where s.staffCode like %?1% or s.displayName like %?1%")
	public Page<StaffDto> findPageByCodeOrName(String staffCode,Pageable pageable);
}
