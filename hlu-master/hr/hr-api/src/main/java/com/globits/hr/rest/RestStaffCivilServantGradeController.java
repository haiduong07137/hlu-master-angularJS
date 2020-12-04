package com.globits.hr.rest;

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
import com.globits.hr.dto.StaffCivilServantGradeDto;
import com.globits.hr.service.StaffCivilServantGradeService;

@RestController
@RequestMapping("/api/staffcivilservantgrade")
public class RestStaffCivilServantGradeController {
	@Autowired
	private StaffCivilServantGradeService staffCivilServantGradeService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/list/{pageIndex}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<StaffCivilServantGradeDto>> getAllDocuments(@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageSize <= 0) {
			pageSize = 25;
		}

		Page<StaffCivilServantGradeDto> documents = staffCivilServantGradeService.getPage(pageIndex, pageSize);

		return new ResponseEntity<Page<StaffCivilServantGradeDto>>(documents, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public StaffCivilServantGradeDto saveStaffCivilServantGrade(
			@RequestBody StaffCivilServantGradeDto staffCivilServantGradeDto) {
		StaffCivilServantGradeDto dto = staffCivilServantGradeService
				.saveStaffCivilServantGrade(staffCivilServantGradeDto);
		return dto;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StaffCivilServantGradeDto> getDocument(@PathVariable("id") Long id) {
		StaffCivilServantGradeDto dto = staffCivilServantGradeService.getStaffCivilServantGrade(id);

		if (dto == null) {
			return new ResponseEntity<StaffCivilServantGradeDto>(new StaffCivilServantGradeDto(),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<StaffCivilServantGradeDto>(dto, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{staffCivilServantGradeId}", method = RequestMethod.DELETE)
	public Boolean removeStaffCivilServantGrade(
			@PathVariable("staffCivilServantGradeId") String staffCivilServantGradeId) {
		Boolean ret = staffCivilServantGradeService.removeStaffCivilServantGrade(new Long(staffCivilServantGradeId));
		return ret;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteStaffCivilServantGrades(@RequestBody StaffCivilServantGradeDto[] dtos) {
		Boolean deleted = staffCivilServantGradeService.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}
}
