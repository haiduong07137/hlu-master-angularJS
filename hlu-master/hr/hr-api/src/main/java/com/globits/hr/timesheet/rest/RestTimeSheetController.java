package com.globits.hr.timesheet.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

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

import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffShiftWorkDto;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.service.TimeSheetService;


@RestController
@RequestMapping("/api/timesheet")
public class RestTimeSheetController {
	@Autowired
	private TimeSheetService timeSheetService;

	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TimeSheetDto> findTimeSheetById(@PathVariable("id") Long id) {
		TimeSheetDto result = timeSheetService.findTimeSheetById(id);
		return new ResponseEntity<TimeSheetDto>(result, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/workingdate/{workingdate}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TimeSheetDto>> getTimeSheetByWorkingDate(@PathVariable String workingdate ,@PathVariable int pageIndex, @PathVariable int pageSize) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		try {
			date = format.parse(workingdate);
		} catch (ParseException e) {

			e.printStackTrace();
		} 
		Page<TimeSheetDto> result = timeSheetService.getAllByWorkingDate(date,pageSize,pageIndex);
		return new ResponseEntity<Page<TimeSheetDto>>(result, HttpStatus.OK);
	}
	
	
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<TimeSheetDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<TimeSheetDto> results = timeSheetService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<TimeSheetDto>>(results, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(method = RequestMethod.POST)
	public  ResponseEntity<TimeSheetDto> saveTimeSheet(@RequestBody TimeSheetDto dto) {
		dto = timeSheetService.saveTimeSheet(dto); 
		return new ResponseEntity<TimeSheetDto>(dto,HttpStatus.OK);
	}
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value="/create",method = RequestMethod.POST)
	public  ResponseEntity<Boolean> createTimeSheets(@RequestBody StaffShiftWorkDto staffShiftWork) {
		Boolean ret = timeSheetService.createTimeSheets(staffShiftWork); 
		return new ResponseEntity<Boolean>(ret,HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public  ResponseEntity<Boolean> deleteTimeSheet(@PathVariable("id") Long id) {
		Boolean ret = timeSheetService.deleteTimeSheetById(id); 
		return new ResponseEntity<Boolean>(ret,HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE)
	public  ResponseEntity<Boolean> deleteTimeSheet(@RequestBody List<TimeSheetDto> list) {
		Boolean ret = timeSheetService.deleteTimeSheets(list); 
		return new ResponseEntity<Boolean>(ret,HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/searchStaff/{name}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<StaffDto>> findPageByName(@PathVariable("name") String name,@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<StaffDto> result = timeSheetService.findPageByName(name,pageIndex,pageSize);
		return new ResponseEntity<Page<StaffDto>>(result, HttpStatus.OK);
	}
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/staff/{name}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<TimeSheetDto>> findPageByStaff(@PathVariable("name") String name,@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<TimeSheetDto> result = timeSheetService.findPageByStaff(name,pageIndex,pageSize);
		return new ResponseEntity<Page<TimeSheetDto>>(result, HttpStatus.OK);
	}

	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value="/searchByDto/{pageIndex}/{pageSize}",method = RequestMethod.POST)
	public ResponseEntity<Page<TimeSheetDto>> searchByDto(@RequestBody SearchTimeSheetDto searchTimeSheetDto,@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<TimeSheetDto> result = timeSheetService.searchByDto(searchTimeSheetDto,pageIndex,pageSize);
		return new ResponseEntity<Page<TimeSheetDto>>(result, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_STAFF"})
	@RequestMapping(value = "/detail/{id}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<TimeSheetDetailDto>> getTimeSheetDetail(@PathVariable Long id,@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<TimeSheetDetailDto> result = timeSheetService.getTimeSheetDetailByTimeSheetID(id,pageIndex,pageSize);
		return new ResponseEntity<Page<TimeSheetDetailDto>>(result, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value="/confirm",method = RequestMethod.POST)
	public  ResponseEntity<Boolean> confirmTimeSheets(@RequestBody List<TimeSheetDto> listdto) {
		Boolean ret = timeSheetService.confirmTimeSheets(listdto); 
		return new ResponseEntity<Boolean>(ret,HttpStatus.OK);
	}
}
