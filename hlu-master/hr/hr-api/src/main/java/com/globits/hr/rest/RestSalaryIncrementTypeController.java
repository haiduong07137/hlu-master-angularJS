/*
 * Created by TA2 & Giang on 23/4/2018.
 */

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
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.service.SalaryIncrementTypeService;

@RestController
@RequestMapping("/api/salaryincrementtype")
public class RestSalaryIncrementTypeController {
	@Autowired
	private SalaryIncrementTypeService salaryIncrementTypeService;

	/*
	 * Get danh sách
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<SalaryIncrementTypeDto>> getPage(@PathVariable int pageIndex,
			@PathVariable int pageSize) {
		Page<SalaryIncrementTypeDto> results = salaryIncrementTypeService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<SalaryIncrementTypeDto>>(results, HttpStatus.OK);
	}

	/*
	 * Post phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<SalaryIncrementTypeDto> saveSalaryIncrementType(@RequestBody SalaryIncrementTypeDto dto) {
		SalaryIncrementTypeDto result = salaryIncrementTypeService.saveSalaryIncrementType(dto);
		return new ResponseEntity<SalaryIncrementTypeDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SalaryIncrementTypeDto> getSalaryIncrementType(@PathVariable Long id) {
		SalaryIncrementTypeDto result = salaryIncrementTypeService.getSalaryIncrementType(id);
		return new ResponseEntity<SalaryIncrementTypeDto>(result, HttpStatus.OK);
	}

	/*
	 * Xóa nhiều phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveSalaryIncrementType(@RequestBody List<SalaryIncrementTypeDto> dtos) {
		int result = salaryIncrementTypeService.deleteSalaryIncrementTypes(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	/*
	 * Xóa 1 phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> SalaryIncrmentType(@PathVariable Long id) {
		Boolean result = salaryIncrementTypeService.deleteSalaryIncrementType(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
