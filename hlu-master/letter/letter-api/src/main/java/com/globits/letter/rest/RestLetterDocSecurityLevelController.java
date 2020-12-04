package com.globits.letter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterDocSecurityLevel;
import com.globits.letter.service.LetterDocSecurityLevelService;

@RestController
@RequestMapping("/api/letter/doc_security_level")
public class RestLetterDocSecurityLevelController {
	@Autowired
	private LetterDocSecurityLevelService letterDocSecurityLevelService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.GET)
	public LetterDocSecurityLevel getCourseHour(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		return letterDocSecurityLevelService.findById(new Long(letterDocIFieldId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocSecurityLevel> getCourseHours(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return letterDocSecurityLevelService.getList(pageIndex, pageSize);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocSecurityLevel saveCourseHour(@RequestBody LetterDocSecurityLevel coursehour) {
		return letterDocSecurityLevelService.save(coursehour);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.DELETE)
	public LetterDocSecurityLevel removeCourseHour(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		LetterDocSecurityLevel coursehour = letterDocSecurityLevelService.delete(new Long(letterDocIFieldId));
		return coursehour;
	}
}
