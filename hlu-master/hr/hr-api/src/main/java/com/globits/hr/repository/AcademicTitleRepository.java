/*
 * TA va Giang làm
 */

package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.AcademicTitle;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.AcademicTitleDto;
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.dto.SalaryItemDto;

@Repository
public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, Long>{
	@Query("select new com.globits.hr.dto.AcademicTitleDto(s) from AcademicTitle s")
	Page<AcademicTitleDto> getListPage( Pageable pageable);
}
