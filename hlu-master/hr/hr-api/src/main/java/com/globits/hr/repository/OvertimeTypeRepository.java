/*
 * Created by TA & Giang on 22/4/2018.
 */

package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.globits.hr.domain.OvertimeType;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.OvertimeTypeDto;
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.dto.SalaryItemDto;

@Repository
public interface OvertimeTypeRepository extends JpaRepository<OvertimeType, Long>{
	@Query("select new com.globits.hr.dto.OvertimeTypeDto(s) from OvertimeType s")
	Page<OvertimeTypeDto> getListPage( Pageable pageable);
}
