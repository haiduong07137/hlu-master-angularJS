package com.globits.hr.timesheet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;

@Repository
public interface TimeSheetDetailRepository extends JpaRepository<TimeSheetDetail, Long> {
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDetailDto(sc) from TimeSheetDetail sc")
	Page<TimeSheetDetailDto> getListPage( Pageable pageable);
	
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDetailDto(ts) from TimeSheetDetail ts where ts.id=?1")
	TimeSheetDetailDto findTimeSheetDetailById(Long id);
}
