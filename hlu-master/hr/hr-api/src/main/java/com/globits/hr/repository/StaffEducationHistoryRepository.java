package com.globits.hr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.StaffEducationHistory;
import com.globits.hr.dto.StaffEducationHistoryDto;

@Repository
public interface StaffEducationHistoryRepository extends JpaRepository<StaffEducationHistory, Long>{

	@Query("select new com.globits.hr.dto.StaffEducationHistoryDto(education) from StaffEducationHistory education where education.id = ?1")
	StaffEducationHistoryDto getEducationById(Long id);
	
	@Query("select new com.globits.hr.dto.StaffEducationHistoryDto(education) from StaffEducationHistory education where education.staff.id = ?1")
	List<StaffEducationHistoryDto> getAll(Long id);
	
	@Query("select new com.globits.hr.dto.StaffEducationHistoryDto(education) from StaffEducationHistory education")
	Page<StaffEducationHistoryDto> getPages(Pageable pageable);

}
