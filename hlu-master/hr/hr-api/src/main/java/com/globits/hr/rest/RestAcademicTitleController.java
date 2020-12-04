/*
 * Giang và Tuấn Anh
 */

package com.globits.hr.rest;
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

import com.globits.hr.dto.AcademicTitleDto;
/*
 * 
 */
import com.globits.hr.service.AcademicTitleService;
import com.globits.core.Constants;
import com.globits.hr.HrConstants;
@RestController
@RequestMapping("/api/academictitle")
public class RestAcademicTitleController {
	@Autowired
	private AcademicTitleService academicTitleService;
/*
 * Get danh sách 
 */
	@Secured({HrConstants.ROLE_HR_MANAGEMENT,Constants.ROLE_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<AcademicTitleDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<AcademicTitleDto> results = academicTitleService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<AcademicTitleDto>>(results, HttpStatus.OK);
	}
/*
 * Post phần tử 
 */
	@Secured({HrConstants.ROLE_HR_MANAGEMENT,Constants.ROLE_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<AcademicTitleDto> saveAcademicTitle(@RequestBody AcademicTitleDto dto) {
		AcademicTitleDto result = academicTitleService.saveAcademicTitle(dto);
		return new ResponseEntity<AcademicTitleDto>(result, HttpStatus.OK);
	}
//	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseEntity<AcademicTitleDto> getAcademicTitle(@PathVariable Long id) {
		AcademicTitleDto result = academicTitleService.getAcademicTitle(id);
		return new ResponseEntity<AcademicTitleDto>(result, HttpStatus.OK);
	}
/*
 * Xóa nhiều phần tử
 */
	@Secured({HrConstants.ROLE_HR_MANAGEMENT,Constants.ROLE_ADMIN})
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveAcademicTitle(@RequestBody List<AcademicTitleDto> dtos) {
		int result = academicTitleService.deleteAcademicTitles(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
//	
/*
 * Xóa 1 phần tử
 */
	@Secured({HrConstants.ROLE_HR_MANAGEMENT,Constants.ROLE_ADMIN})
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> saveAcademicTitle(@PathVariable Long id) {
		Boolean result = academicTitleService.deleteAcademicTitle(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
