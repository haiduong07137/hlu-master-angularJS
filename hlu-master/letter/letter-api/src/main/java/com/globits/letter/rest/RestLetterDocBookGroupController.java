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
import com.globits.letter.domain.LetterDocBookGroup;
import com.globits.letter.dto.LetterDocBookGroupDto;
import com.globits.letter.service.LetterDocBookGroupService;

@RestController
@RequestMapping("/api/letter/docbookgroup")
public class RestLetterDocBookGroupController {

	@Autowired
	private LetterDocBookGroupService letterDocBookGroupService;
	
	@Secured({ "ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookGroupId}", method = RequestMethod.GET)
	public LetterDocBookGroup getDocBookGroup(@PathVariable("letterDocBookGroupId") Long letterDocBookGroupId) {
		return letterDocBookGroupService.findById(letterDocBookGroupId);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<LetterDocBookGroup> getDocBookGroups(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return letterDocBookGroupService.getList(pageIndex, pageSize);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public LetterDocBookGroup saveDocBookGroup(@RequestBody LetterDocBookGroup LetterDocBookGroup) {
		return letterDocBookGroupService.save(LetterDocBookGroup);
	}
	
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookGroupId}", method = RequestMethod.PUT)
	public LetterDocBookGroup updateLetterDocBookGroup(@RequestBody LetterDocBookGroup socialPriority,
			@PathVariable("letterDocBookGroupId") Long letterDocBookGroupId) {
		return letterDocBookGroupService.save(socialPriority);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/{letterDocBookGroupId}", method = RequestMethod.DELETE)
	public LetterDocBookGroup removeDocBookGroup(@PathVariable("letterDocBookGroupId") String letterDocBookGroupId) {
		LetterDocBookGroup LetterDocBookGroup = letterDocBookGroupService.delete(new Long(letterDocBookGroupId));
		return LetterDocBookGroup;
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/checkCode/{code}",method = RequestMethod.GET)
	public LetterDocBookGroupDto checkDuplicateCode(@PathVariable("code") String code) {
		return letterDocBookGroupService.checkDuplicateCode(code);
	}
}
