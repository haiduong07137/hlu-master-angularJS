/*
 * TA va Giang làm
 */

package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.dto.SalaryIncrementTypeDto;

@Repository
public interface SalaryIncrementTypeRepository extends JpaRepository<SalaryIncrementType, Long>{
	@Query("select new com.globits.hr.dto.SalaryIncrementTypeDto(s) from SalaryIncrementType s")
	Page<SalaryIncrementTypeDto> getListPage( Pageable pageable);
}
