/*
 * Created by TA & Giang on 22/4/2018.
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
import com.globits.hr.dto.OvertimeTypeDto;
import com.globits.hr.service.OvertimeTypeService;

@RestController
@RequestMapping("/api/overtimetype")
public class RestOvertimeTypeController {
	@Autowired
	private OvertimeTypeService overtimeTypeService;

	/*
	 * Get danh sách
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<OvertimeTypeDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<OvertimeTypeDto> results = overtimeTypeService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<OvertimeTypeDto>>(results, HttpStatus.OK);
	}

	/*
	 * Post phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<OvertimeTypeDto> saveOvertimeType(@RequestBody OvertimeTypeDto dto) {
		OvertimeTypeDto result = overtimeTypeService.saveOvertimeType(dto);
		return new ResponseEntity<OvertimeTypeDto>(result, HttpStatus.OK);
	}

	//
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OvertimeTypeDto> getOvertimeType(@PathVariable Long id) {
		OvertimeTypeDto result = overtimeTypeService.getOvertimeType(id);
		return new ResponseEntity<OvertimeTypeDto>(result, HttpStatus.OK);
	}

	/*
	 * Xóa nhiều phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveOvertimeType(@RequestBody List<OvertimeTypeDto> dtos) {
		int result = overtimeTypeService.deleteOvertimeTypes(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	//
	/*
	 * Xóa 1 phần tử
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> saveOvertimeType(@PathVariable Long id) {
		Boolean result = overtimeTypeService.deleteOvertimeType(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
