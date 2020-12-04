package com.globits.calendar.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.DepartmentSearchDto;
import com.globits.core.dto.DepartmentTreeDto;
import com.globits.core.service.DepartmentService;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.StaffService;

@RestController
@RequestMapping(path = "/api/calendar/hr")
public class RestHrController {
	@Autowired
	StaffService staffService;
	@Autowired
	DepartmentService departmentService;
	
	@RequestMapping(value = "/searchstaff/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public Page<StaffDto> searchStaff(@RequestBody StaffSearchDto dto,@PathVariable int pageSize,@PathVariable int pageIndex){
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return staffService.searchStaff(dto, pageSize, pageIndex);
	}
	
	@RequestMapping(value = "/searchdepartment/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public Page<DepartmentDto> searchDepartment(@RequestBody DepartmentSearchDto dto,@PathVariable int pageSize,@PathVariable int pageIndex){
		return departmentService.searchDepartment(dto,pageSize, pageIndex);
	}
	
	@RequestMapping(value = "/departmenttree", method = RequestMethod.GET)
	public List<DepartmentTreeDto> getTreeData() {
		return departmentService.getTreeData();
	}
	
	@RequestMapping(value = "/find/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<StaffDto>> findStaffs(@RequestBody StaffSearchDto dto,
			@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		Page<StaffDto> page = staffService.searchStaff(dto, pageSize,pageIndex);
		return new ResponseEntity<Page<StaffDto>>(page, HttpStatus.OK);
	}
}
