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
import com.globits.letter.domain.LetterDocPriority;
import com.globits.letter.service.LetterDocPriorityService;

@RestController
@RequestMapping("/api/letter/doc_priority")
public class RestLetterDocPriorityController {
	@Autowired
	private LetterDocPriorityService letterDocPriorityService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.GET)
	public LetterDocPriority getDocPriority(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		return letterDocPriorityService.findById(new Long(letterDocIFieldId));
	}

	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocPriority> getDocPriorities(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return letterDocPriorityService.getList(pageIndex, pageSize);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocPriority saveDocPriority(@RequestBody LetterDocPriority docPriority) {
		return letterDocPriorityService.save(docPriority);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.DELETE)
	public LetterDocPriority removeDocPriority(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		LetterDocPriority docPriority = letterDocPriorityService.delete(new Long(letterDocIFieldId));
		return docPriority;
	}
}
