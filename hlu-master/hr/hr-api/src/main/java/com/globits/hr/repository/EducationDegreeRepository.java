package com.globits.hr.repository;
/*
 * author Giang-Tuan Anh
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.EducationDegree;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.EducationDegreeDto;
import com.globits.hr.dto.SalaryItemDto;

@Repository
public interface EducationDegreeRepository extends JpaRepository<EducationDegree, Long>{
	@Query("select new com.globits.hr.dto.EducationDegreeDto(ed) from EducationDegree ed")
	Page<EducationDegreeDto> getListPage( Pageable pageable);
	
	@Query("select new com.globits.hr.dto.EducationDegreeDto(ed) from EducationDegree ed where ed.name like ?1 or ed.code like ?2")
	Page<EducationDegreeDto> searchByPage(String name, String code, Pageable pageable);
}
