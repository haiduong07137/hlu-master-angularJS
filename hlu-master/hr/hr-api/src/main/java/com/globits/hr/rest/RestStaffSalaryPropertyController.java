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
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSalaryPropertyDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.SalaryItemService;
import com.globits.hr.service.StaffSalaryPropertyService;
import com.globits.hr.service.StaffService;

@RestController
@RequestMapping("/api/staffsalaryproperty")
public class RestStaffSalaryPropertyController {
	@Autowired
	private StaffSalaryPropertyService staffSalaryPropertyService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<StaffSalaryPropertyDto>> getPage(@PathVariable int pageIndex,
			@PathVariable int pageSize) {
		Page<StaffSalaryPropertyDto> results = staffSalaryPropertyService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<StaffSalaryPropertyDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<StaffSalaryPropertyDto> saveStaffSalaryProperty(@RequestBody StaffSalaryPropertyDto dto) {
		StaffSalaryPropertyDto result = staffSalaryPropertyService.saveStaffSalaryProperty(dto);
		return new ResponseEntity<StaffSalaryPropertyDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<StaffSalaryPropertyDto> getStaffSalaryProperty(@PathVariable Long id) {
		StaffSalaryPropertyDto result = staffSalaryPropertyService.getStaffSalaryProperty(id);
		return new ResponseEntity<StaffSalaryPropertyDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveStaffSalaryProperty(@RequestBody List<StaffSalaryPropertyDto> dtos) {
		int result = staffSalaryPropertyService.deleteStaffSalaryPropertys(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> saveSalaryItem(@PathVariable Long id) {
		Boolean result = staffSalaryPropertyService.deleteStaffSalaryProperty(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
