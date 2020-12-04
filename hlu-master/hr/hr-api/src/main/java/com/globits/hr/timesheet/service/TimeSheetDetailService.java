package com.globits.hr.timesheet.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;

public interface TimeSheetDetailService extends GenericService<TimeSheetDetail, Long>{
	
	public TimeSheetDetailDto saveTimeSheetDetail(TimeSheetDetailDto dto);
	public Page<TimeSheetDetailDto> getPage(int pageSize, int pageIndex);
	public Boolean deleteTimeSheetDetails(List<TimeSheetDetailDto> list);
	public TimeSheetDetailDto findTimeSheetDetailById(Long id);

}
