package com.globits.letter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.dto.LetterDocBookDto;
import com.globits.letter.service.LetterDocBookService;

@RestController
@RequestMapping("/api/letter/docbook")
public class RestLetterDocBookController {
	@Autowired
	private LetterDocBookService letterDocBookService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookId}", method = RequestMethod.GET)
	public LetterDocBook getDocBook(@PathVariable("letterDocBookId") Long letterDocBookId) {
		return letterDocBookService.findById(new Long(letterDocBookId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocBook> getDocBooks(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return letterDocBookService.getList(pageIndex, pageSize);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "getByGroupId/{groupId}", method = RequestMethod.GET)
	public List<LetterDocBook> getDocBooksByGroupId(@PathVariable("groupId") Long groupId) {
		return letterDocBookService.getDocBooksByGroupId(groupId);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocBook saveDocBook(@RequestBody LetterDocBook letterDocBook) {
		return letterDocBookService.save(letterDocBook);
	}
	
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookId}", method = RequestMethod.PUT)
	public LetterDocBook updateLetterDocBook(@RequestBody LetterDocBook socialPriority,
			@PathVariable("letterDocBookId") Long letterDocBookId) {
		return letterDocBookService.save(socialPriority);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookId}", method = RequestMethod.DELETE)
	public LetterDocBook removeDocBook(@PathVariable("letterDocBookId") String letterDocBookId) {
		LetterDocBook letterDocBook = letterDocBookService.delete(new Long(letterDocBookId));
		return letterDocBook;
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/checkCode/{code}",method = RequestMethod.GET)
	public LetterDocBookDto checkDuplicateCode(@PathVariable("code") String code) {
		return letterDocBookService.checkDuplicateCode(code);
	}
}
