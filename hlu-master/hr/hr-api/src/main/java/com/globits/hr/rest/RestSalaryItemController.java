package com.globits.hr.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.SalaryItemService;
import com.globits.hr.service.StaffService;

@RestController
@RequestMapping("/api/salaryitem")
public class RestSalaryItemController {
	@Autowired
	private SalaryItemService salaryItemService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<SalaryItemDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<SalaryItemDto> results = salaryItemService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<SalaryItemDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<SalaryItemDto> saveSalaryItem(@RequestBody SalaryItemDto dto) {
		SalaryItemDto result = salaryItemService.saveSalaryItem(dto);
		return new ResponseEntity<SalaryItemDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SalaryItemDto> getSalaryItem(@PathVariable Long id) {
		SalaryItemDto result = salaryItemService.getSalaryItem(id);
		return new ResponseEntity<SalaryItemDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveSalaryItem(@RequestBody List<SalaryItemDto> dtos) {
		int result = salaryItemService.deleteSalaryItems(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteSalaryItem(@PathVariable Long id) {
		Boolean result = salaryItemService.deleteSalaryItem(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "search/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<SalaryItemDto>> searchSalaryItem(@RequestBody SearchSalaryItemDto dto,
			@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<SalaryItemDto> result = salaryItemService.searchSalaryItem(dto, pageIndex, pageSize);
		return new ResponseEntity<Page<SalaryItemDto>>(result, HttpStatus.OK);
	}
}
