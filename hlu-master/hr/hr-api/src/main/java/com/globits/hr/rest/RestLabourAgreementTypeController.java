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

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.LabourAgreementTypeDto;
import com.globits.hr.dto.SearchLabourAgreementTypeDto;
import com.globits.hr.service.LabourAgreementTypeService;

@RestController
@RequestMapping("/api/labouragreement")
public class RestLabourAgreementTypeController {
	@Autowired
	private LabourAgreementTypeService labourAgreementService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<LabourAgreementTypeDto>> getPage(@PathVariable int pageIndex,
			@PathVariable int pageSize) {
		Page<LabourAgreementTypeDto> results = labourAgreementService.getPage(pageSize, pageIndex);
		return new ResponseEntity<Page<LabourAgreementTypeDto>>(results, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<LabourAgreementTypeDto> saveLabourAgreementType(@RequestBody LabourAgreementTypeDto dto) {
		LabourAgreementTypeDto result = labourAgreementService.saveLabourAgreementType(dto);
		return new ResponseEntity<LabourAgreementTypeDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<LabourAgreementTypeDto> getLabourAgreementType(@PathVariable Long id) {
		LabourAgreementTypeDto result = labourAgreementService.getLabourAgreementType(id);
		return new ResponseEntity<LabourAgreementTypeDto>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Integer> saveLabourAgreementType(@RequestBody List<LabourAgreementTypeDto> dtos) {
		int result = labourAgreementService.deleteLabourAgreementTypes(dtos);
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteLabourAgreementType(@PathVariable Long id) {
		Boolean result = labourAgreementService.deleteLabourAgreementType(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "search/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<LabourAgreementTypeDto>> searchLabourAgreementTypeDto(
			@RequestBody SearchLabourAgreementTypeDto dto, @PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<LabourAgreementTypeDto> result = labourAgreementService.searchLabourAgreementType(dto, pageIndex,
				pageSize);
		return new ResponseEntity<Page<LabourAgreementTypeDto>>(result, HttpStatus.OK);
	}
}
