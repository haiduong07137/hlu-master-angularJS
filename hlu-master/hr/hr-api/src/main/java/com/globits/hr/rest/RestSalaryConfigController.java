package com.globits.hr.rest;

/*
 * Giang và Tuấn Anh
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.service.PositionTitleService;
/*
 * Quản trị công thức tính lương
 */
import com.globits.hr.service.SalaryConfigService;

@RestController
@RequestMapping("/api/salaryconfig")
public class RestSalaryConfigController {
	@Autowired
	private SalaryConfigService salaryConfigService;

	/*
	 * Get danh sách phần tử lương
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<SalaryConfigDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<SalaryConfigDto> results = salaryConfigService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<SalaryConfigDto>>(results, HttpStatus.OK);
	}

	/*
	 * Post phần tử lương
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<SalaryConfigDto> saveSalaryConfig(@RequestBody SalaryConfigDto dto) {
		SalaryConfigDto result = salaryConfigService.saveSalaryConfig(dto);
		return new ResponseEntity<SalaryConfigDto>(result, HttpStatus.OK);
	}

	//
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SalaryConfigDto> getSalaryConfig(@PathVariable Long id) {
		SalaryConfigDto result = salaryConfigService.getSalaryConfig(id);
		return new ResponseEntity<SalaryConfigDto>(result, HttpStatus.OK);
	}

	/*
	 * Xóa nhiều phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveSalaryConfig(@RequestBody List<SalaryConfigDto> dtos) {
		int result = salaryConfigService.deleteSalaryConfig(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	//
	/*
	 * Xóa 1 phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> saveSalaryConfig(@PathVariable Long id) {
		Boolean result = salaryConfigService.deleteSalaryConfig(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

}
