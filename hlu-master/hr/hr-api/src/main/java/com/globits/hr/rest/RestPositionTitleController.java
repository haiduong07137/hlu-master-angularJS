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
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.service.PositionTitleService;
/*
 * Quản trị học hàm, học vị
 */

@RestController
@RequestMapping("/api/positiontitle")
public class RestPositionTitleController {
	@Autowired
	private PositionTitleService titleService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/list/{pageIndex}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<PositionTitleDto>> getAllDocuments(@PathVariable int pageIndex,
			@PathVariable int pageSize) {

		if (pageIndex < 0) {
			pageIndex = 0;
		}

		if (pageSize <= 0) {
			pageSize = 25;
		}

		Page<PositionTitleDto> documents = titleService.getPage(pageIndex, pageSize);

		return new ResponseEntity<Page<PositionTitleDto>>(documents, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public PositionTitleDto saveTitle(@RequestBody PositionTitleDto titleDto) {
		PositionTitleDto dto = titleService.saveTitle(titleDto);
		return dto;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PositionTitleDto> getDocument(@PathVariable("id") Long id) {
		PositionTitleDto dto = titleService.getTitle(id);

		if (dto == null) {
			return new ResponseEntity<PositionTitleDto>(new PositionTitleDto(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<PositionTitleDto>(dto, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{titleId}", method = RequestMethod.DELETE)
	public Boolean removeTitle(@PathVariable("titleId") String titleId) {
		Boolean ret = titleService.removeTitle(new Long(titleId));
		return ret;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteTitles(@RequestBody PositionTitleDto[] dtos) {
		Boolean deleted = titleService.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}
}
