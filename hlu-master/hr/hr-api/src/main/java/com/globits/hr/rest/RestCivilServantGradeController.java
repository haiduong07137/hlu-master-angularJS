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
import com.globits.hr.dto.CivilServantGradeDto;
import com.globits.hr.service.CivilServantGradeService;

/*
 * Bậc công chức
 */
@RestController
@RequestMapping("/api/civilservantgrade")
public class RestCivilServantGradeController {
	@Autowired
	private CivilServantGradeService civilServantGradeService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/list/{pageIndex}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CivilServantGradeDto>> getAllDocuments(@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageSize <= 0) {
			pageSize = 25;
		}

		Page<CivilServantGradeDto> documents = civilServantGradeService.getPage(pageIndex, pageSize);

		return new ResponseEntity<Page<CivilServantGradeDto>>(documents, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public CivilServantGradeDto saveCivilServantGrade(@RequestBody CivilServantGradeDto civilServantGradeDto) {
		CivilServantGradeDto dto = civilServantGradeService.saveCivilServantGrade(civilServantGradeDto);
		return dto;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CivilServantGradeDto> getDocument(@PathVariable("id") Long id) {
		CivilServantGradeDto dto = civilServantGradeService.getCivilServantGrade(id);

		if (dto == null) {
			return new ResponseEntity<CivilServantGradeDto>(new CivilServantGradeDto(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<CivilServantGradeDto>(dto, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{civilServantGradeId}", method = RequestMethod.DELETE)
	public Boolean removeCivilServantGrade(@PathVariable("civilServantGradeId") String civilServantGradeId) {
		Boolean ret = civilServantGradeService.removeCivilServantGrade(new Long(civilServantGradeId));
		return ret;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteCivilServantGrades(@RequestBody CivilServantGradeDto[] dtos) {
		Boolean deleted = civilServantGradeService.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}
}
