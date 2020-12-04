package com.globits.hr.timesheet.service;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.dto.SearchReportDto;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffShiftWorkDto;
import com.globits.hr.dto.SynthesisReportOfStaffDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;

public interface TimeSheetService extends GenericService<TimeSheet, Long>{
	
	public Page<TimeSheetDto> getPage(int pageSize, int pageIndex);
	public TimeSheetDto saveTimeSheet(TimeSheetDto dto);
	public TimeSheetDto findTimeSheetByDate(Date date);
	public TimeSheetDto findTimeSheetById(Long id);
	public Boolean deleteTimeSheetById(Long id);
	public Boolean deleteTimeSheets(List<TimeSheetDto> list);
	public Page<StaffDto> findPageByName(String textSearch, int pageIndex, int pageSize);
	public Page<TimeSheetDto> getAllByWorkingDate(Date workingDate, int pageIndex, int pageSize);
	public Page<TimeSheetDetailDto> getTimeSheetDetailByTimeSheetID(Long id, int pageIndex, int pageSize);
	public Boolean confirmTimeSheets(List<TimeSheetDto> list);
	public Page<TimeSheetDto> findPageByStaff(String textSearch, int pageIndex, int pageSize);
	public Boolean createTimeSheets(StaffShiftWorkDto staffShiftwork);
	public Page<TimeSheetDto> searchByDto(SearchTimeSheetDto searchTimeSheetDto, int pageIndex, int pageSize);
	public Page<SynthesisReportOfStaffDto> reportWorkingStatus(SearchReportDto searchReportDto, int pageIndex,
			int pageSize);
}
