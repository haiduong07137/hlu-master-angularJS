package com.globits.hr.timesheet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import java.util.Date;
@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDto(ts) from TimeSheet ts where ts.id=?1")
	TimeSheetDto findTimeSheetById(Long id);
	
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDetailDto(tsd) from TimeSheetDetail tsd where tsd.timeSheet.id=?1")
	 Page<TimeSheetDetailDto> findTimeSheetDetailByTimeSheetId(Long id, Pageable pageable);
	
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDto(sc) from TimeSheet sc")
	Page<TimeSheetDto> getListPage( Pageable pageable);
	
	@Query("from TimeSheet ts where date(ts.workingDate)=date(?1)")
	TimeSheet findByDate(Date date);
	
	@Query("select new com.globits.hr.dto.StaffDto(s) from Staff s where s.displayName like ?1")
	public Page<StaffDto> findPageByName(String staffCode,Pageable pageable);
	
	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDto(s) from TimeSheet s where s.employee.displayName like %?1% or s.employee.staffCode like %?1%")
	public Page<TimeSheetDto> findPageByCodeOrName(String staffCode,Pageable pageable);

	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDto(s) from TimeSheet s where s.workingDate = ?1 ")
	public Page<TimeSheetDto> findPageByDate(Date workingDate, Pageable pageable);

	@Query("select new com.globits.hr.timesheet.dto.TimeSheetDto(s) from TimeSheet s where (s.employee.displayName like %?1% or s.employee.staffCode like %?1%) and s.workingDate = ?2 ")
	Page<TimeSheetDto> findPageByCodeAndNameAndDate(String codeAndName, Date workingDate, Pageable pageable);
}
