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
import com.globits.hr.service.CivilServantCategoryService;

/*
 * Quản lý ngạch công chức
 */
@RestController
@RequestMapping("/api/civilservantcategory")
public class RestCivilServantCategoryController {
	@Autowired
	private CivilServantCategoryService civilServantCategoryService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/list/{pageIndex}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CivilServantCategoryDto>> getListCategory(@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		 if (pageIndex < 0) {
		 pageIndex = 0;
		 }
		
		 if (pageSize <= 0) {
		 pageSize = 25;
		 }

		Page<CivilServantCategoryDto> documents = civilServantCategoryService.getPage(pageIndex, pageSize);

		return new ResponseEntity<Page<CivilServantCategoryDto>>(documents, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public CivilServantCategoryDto saveCivilServantCategory(
			@RequestBody CivilServantCategoryDto civilServantCategoryDto) {
		CivilServantCategoryDto dto = civilServantCategoryService.saveCivilServantCategory(civilServantCategoryDto);
		return dto;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CivilServantCategoryDto> getDocument(@PathVariable("id") Long id) {
		CivilServantCategoryDto dto = civilServantCategoryService.getCivilServantCategory(id);

		if (dto == null) {
			return new ResponseEntity<CivilServantCategoryDto>(new CivilServantCategoryDto(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<CivilServantCategoryDto>(dto, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{civilServantCategoryId}", method = RequestMethod.DELETE)
	public Boolean removeCivilServantCategory(@PathVariable("civilServantCategoryId") String civilServantCategoryId) {
		Boolean ret = civilServantCategoryService.removeCivilServantCategory(new Long(civilServantCategoryId));
		return ret;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteCivilServantCategorys(@RequestBody CivilServantCategoryDto[] dtos) {
		Boolean deleted = civilServantCategoryService.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}
}
