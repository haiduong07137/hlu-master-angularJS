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
import com.globits.hr.dto.CivilServantCategoryDto;
import com.globits.hr.dto.CivilServantCategoryGradeDto;
import com.globits.hr.service.CivilServantCategoryGradeService;
import com.globits.hr.service.CivilServantCategoryService;
/*
 * Quản lý ngạch bậc công chức (ngạch + bậc => một hệ số lương)
 */

@RestController
@RequestMapping("/api/civilservantcategorygrade")
public class RestCivilServantCategoryGradeController {
	@Autowired
	private CivilServantCategoryGradeService civilServantCategoryGradeService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/list/{pageIndex}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CivilServantCategoryGradeDto>> getAllDocuments(@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageSize <= 0) {
			pageSize = 25;
		}

		Page<CivilServantCategoryGradeDto> documents = civilServantCategoryGradeService.getPage(pageIndex, pageSize);

		return new ResponseEntity<Page<CivilServantCategoryGradeDto>>(documents, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public CivilServantCategoryGradeDto saveCivilServantCategoryGrade(
			@RequestBody CivilServantCategoryGradeDto civilServantCategoryGradeDto) {
		CivilServantCategoryGradeDto dto = civilServantCategoryGradeService
				.saveCivilServantCategoryGrade(civilServantCategoryGradeDto);
		return dto;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CivilServantCategoryGradeDto> getDocument(@PathVariable("id") Long id) {
		CivilServantCategoryGradeDto dto = civilServantCategoryGradeService.getCivilServantCategoryGrade(id);

		if (dto == null) {
			return new ResponseEntity<CivilServantCategoryGradeDto>(new CivilServantCategoryGradeDto(),
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<CivilServantCategoryGradeDto>(dto, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{civilServantCategoryGradeId}", method = RequestMethod.DELETE)
	public Boolean removeCivilServantCategory(
			@PathVariable("civilServantCategoryGradeId") String civilServantCategoryId) {
		Boolean ret = civilServantCategoryGradeService
				.removeCivilServantCategoryGrade(new Long(civilServantCategoryId));
		return ret;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteCivilServantCategorys(@RequestBody CivilServantCategoryGradeDto[] dtos) {
		Boolean deleted = civilServantCategoryGradeService.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}
}
