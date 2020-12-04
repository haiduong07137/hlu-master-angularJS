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

import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterDocField;
import com.globits.letter.service.LetterDocFieldService;

@RestController
@RequestMapping("/api/letter/doc_field")
public class RestLetterDocFieldController {
	@Autowired
	private LetterDocFieldService letterDocFieldService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.GET)
	public LetterDocField getDocField(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		return letterDocFieldService.findById(new Long(letterDocIFieldId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocField> getDocFields(@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<LetterDocField> result = letterDocFieldService.getList(pageIndex, pageSize);
		return result;
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocField saveDocField(@RequestBody LetterDocField docField) {
		return letterDocFieldService.save(docField);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocIFieldId}", method = RequestMethod.DELETE)
	public LetterDocField removeDocField(@PathVariable("letterDocIFieldId") String letterDocIFieldId) {
		LetterDocField docField = letterDocFieldService.delete(new Long(letterDocIFieldId));
		return docField;
	}
}
