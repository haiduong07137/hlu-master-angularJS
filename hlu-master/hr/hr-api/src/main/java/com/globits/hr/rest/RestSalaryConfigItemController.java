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
import com.globits.hr.service.SalaryConfigItemService;
/*
 * Quản trị công thức tính lương
 */
import com.globits.hr.service.SalaryConfigService;

@RestController
@RequestMapping("/api/salaryconfigitem")
public class RestSalaryConfigItemController {
	@Autowired
	private SalaryConfigItemService salaryConfigItemService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{salaryConfigId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<SalaryConfigItemDto>> getPageBySalaryConfigId(@PathVariable Long salaryConfigId,
			@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<SalaryConfigItemDto> results = salaryConfigItemService.getPageBySalaryConfigId(salaryConfigId, pageSize,
				pageIndex);
		return new ResponseEntity<Page<SalaryConfigItemDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SalaryConfigItemDto> getSalaryConfigItem(@PathVariable Long id) {
		SalaryConfigItemDto result = salaryConfigItemService.getSalaryConfigItem(id);
		return new ResponseEntity<SalaryConfigItemDto>(result, HttpStatus.OK);
	}

	/*
	 * Post chi tiết phần tử lương
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<SalaryConfigItemDto> saveSalaryConfigItem(@RequestBody SalaryConfigItemDto dto) {
		SalaryConfigItemDto result = salaryConfigItemService.saveSalaryConfigItem(dto);
		return new ResponseEntity<SalaryConfigItemDto>(result, HttpStatus.OK);
	}
}
