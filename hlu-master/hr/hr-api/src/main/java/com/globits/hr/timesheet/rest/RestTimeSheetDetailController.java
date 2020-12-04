package com.globits.hr.timesheet.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.service.TimeSheetDetailService;

@RestController
@RequestMapping("/api/timesheetdetail")
public class RestTimeSheetDetailController {
	
	@Autowired
	private TimeSheetDetailService timeSheetDetailService;
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<TimeSheetDetailDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<TimeSheetDetailDto> results = timeSheetDetailService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<TimeSheetDetailDto>>(results, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TimeSheetDetailDto> findTimeSheetDetailById(@PathVariable("id") Long id) {
		TimeSheetDetailDto result = timeSheetDetailService.findTimeSheetDetailById(id);
		return new ResponseEntity<TimeSheetDetailDto>(result, HttpStatus.OK);
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public  ResponseEntity<TimeSheetDetailDto> saveTimeSheet(@RequestBody TimeSheetDetailDto dto) {
		dto = timeSheetDetailService.saveTimeSheetDetail(dto); 
		return new ResponseEntity<TimeSheetDetailDto>(dto,HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE)
	public  ResponseEntity<Boolean> deleteTimeSheetDetail(@RequestBody List<TimeSheetDetailDto> list) {
		Boolean ret = timeSheetDetailService.deleteTimeSheetDetails(list); 
		return new ResponseEntity<Boolean>(ret,HttpStatus.OK);
	}
}
