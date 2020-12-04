package com.globits.letter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.dto.OrganizationDto;
import com.globits.core.dto.PersonDto;
import com.globits.letter.LetterConstant;
import com.globits.letter.dto.LetterDocumentDto;
import com.globits.letter.dto.OrganizationTreeDto;
import com.globits.letter.service.LetterCoreService;
import com.globits.security.domain.User;
import com.globits.security.dto.UserDto;
import com.globits.security.service.UserService;

@RestController
@RequestMapping("/api/letter/core")
public class RestLetterCoreController {
	@Autowired
	LetterCoreService service;
	@Autowired
	UserService  userService;
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/organization_tree", method = RequestMethod.GET)
	public List<OrganizationTreeDto> getTreeData() {
		return service.getTreeData();
	}
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/organization/dto/{organizationId}", method = RequestMethod.GET)
	public OrganizationDto getOrganizationById(@PathVariable Long organizationId) {
		return service.getOrganizationById(organizationId);
	}
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/get_person/{staffId}", method = RequestMethod.GET)
	public PersonDto getPersonFromStaff(@PathVariable Long staffId) {
		return service.getPersonByStaff(staffId);
	}
	@RequestMapping(path="/user/{pageSize}/{pageIndex}",method = RequestMethod.GET)
	public Page<UserDto> getUser( @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
		return userService.findByPageBasicInfo(pageIndex, pageSize);
	}
}
