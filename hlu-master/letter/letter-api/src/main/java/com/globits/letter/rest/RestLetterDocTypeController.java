package com.globits.letter.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.dto.DepartmentTreeDto;
import com.globits.core.dto.OrganizationDto;
import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterDocField;
import com.globits.letter.domain.LetterDocumentType;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.OrganizationTreeDto;
import com.globits.letter.service.LetterCoreService;
import com.globits.letter.service.LetterDocumentTypeService;
import com.globits.letter.service.LetterInDocumentService;
import com.globits.taskman.TaskManConstant;

@RestController
@RequestMapping("/api/letter/doc_type")
public class RestLetterDocTypeController {
	@Autowired
	private LetterDocumentTypeService LetterDocumentTypeService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.GET)
	public LetterDocumentType getCourseHour(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		return LetterDocumentTypeService.findById(new Long(letterDocIFieldId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocumentType> getCourseHours(@PathVariable int pageIndex, @PathVariable int pageSize) {
       Page<LetterDocumentType> result = LetterDocumentTypeService.getList(pageIndex, pageSize);
       return result;
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocumentType saveCourseHour(@RequestBody LetterDocumentType coursehour) {
		return LetterDocumentTypeService.save(coursehour);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.DELETE)
	public LetterDocumentType removeCourseHour(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		LetterDocumentType coursehour = LetterDocumentTypeService.delete(new Long(letterDocIFieldId));
		return coursehour;
	}
}
