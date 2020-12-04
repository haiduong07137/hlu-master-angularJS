/*
 * Giang và Tuấn Anh
 */

package com.globits.hr.rest;

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
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.service.AcademicTitleService;
import com.globits.hr.service.ShiftWorkTimePeriodService;

@RestController
@RequestMapping("/api/shiftworktimeperiod")
public class RestShiftWorkTimePeriodController {
	@Autowired
	private ShiftWorkTimePeriodService shiftWorkTimePeriodService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<ShiftWorkTimePeriodDto>> getPage(@PathVariable int pageIndex,
			@PathVariable int pageSize) {
		System.out.print(123);
		Page<ShiftWorkTimePeriodDto> results = shiftWorkTimePeriodService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<ShiftWorkTimePeriodDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ShiftWorkTimePeriodDto> saveShiftWorkTimePeriod(@RequestBody ShiftWorkTimePeriodDto dto) {
		ShiftWorkTimePeriodDto result = shiftWorkTimePeriodService.saveShiftWorkTimePeriod(dto);
		return new ResponseEntity<ShiftWorkTimePeriodDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ShiftWorkTimePeriodDto> getShiftWorkTimePeriod(@PathVariable Long id) {
		ShiftWorkTimePeriodDto result = shiftWorkTimePeriodService.getShiftWorkTimePeriod(id);
		return new ResponseEntity<ShiftWorkTimePeriodDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> delShiftWorkTimePeriods(@RequestBody List<ShiftWorkTimePeriodDto> dtos) {
		int result = shiftWorkTimePeriodService.deleteShiftWorkTimePeriods(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> saveAcademicTitle(@PathVariable Long id) {
		Boolean result = shiftWorkTimePeriodService.deleteShiftWorkTimePeriod(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
