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
import com.globits.hr.dto.EducationDegreeDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.EducationDegreeService;
import com.globits.hr.service.SalaryItemService;
import com.globits.hr.service.StaffService;

@RestController
@RequestMapping("/api/educationdegree")
public class RestEducationDegreeController {
	@Autowired
	private EducationDegreeService educationDegreeService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<EducationDegreeDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<EducationDegreeDto> results = educationDegreeService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<EducationDegreeDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<EducationDegreeDto> saveEducationDegree(@RequestBody EducationDegreeDto dto) {
		EducationDegreeDto result = educationDegreeService.saveEducationDegree(dto);
		return new ResponseEntity<EducationDegreeDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<EducationDegreeDto> getEducationDegree(@PathVariable Long id) {
		EducationDegreeDto result = educationDegreeService.getEducationDegree(id);
		return new ResponseEntity<EducationDegreeDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveEducationDegree(@RequestBody List<EducationDegreeDto> dtos) {
		int result = educationDegreeService.deleteEducationDegrees(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteEducationDegree(@PathVariable Long id) {
		Boolean result = educationDegreeService.deleteEducationDegree(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	/*
	 * Dung searchSalaryitemDto de search cho SearchEducationDegreeDto
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "search/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<EducationDegreeDto>> searchEducationDegree(@RequestBody SearchSalaryItemDto dto,
			@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<EducationDegreeDto> result = educationDegreeService.searchEducationDegree(dto, pageIndex, pageSize);
		return new ResponseEntity<Page<EducationDegreeDto>>(result, HttpStatus.OK);
	}
}
