package com.globits.hr.rest;

import java.util.ArrayList;
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

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.service.WorkingStatusService;
import com.globits.hr.view.ViewOrderDto;

@RestController
@RequestMapping("/api/workingstatus")
public class RestWorkingStatusController {
	@Autowired
	WorkingStatusService workingStatusService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<WorkingStatusDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<WorkingStatusDto> results = workingStatusService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<WorkingStatusDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<WorkingStatusDto> saveWorkingStatus(@RequestBody WorkingStatusDto dto) {
		WorkingStatusDto result = workingStatusService.saveWorkingStatus(dto);
		return new ResponseEntity<WorkingStatusDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<WorkingStatusDto> getWorkingStatus(@PathVariable Long id) {
		WorkingStatusDto result = workingStatusService.getWorkingStatus(id);
		return new ResponseEntity<WorkingStatusDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveWorkingStatus(@RequestBody List<WorkingStatusDto> dtos) {
		int result = workingStatusService.deleteWorkingStatuses(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteWorkingStatus(@PathVariable Long id) {
		Boolean result = workingStatusService.deleteWorkingStatus(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

}
