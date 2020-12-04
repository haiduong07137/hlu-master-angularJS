package com.globits.letter.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.ViewDocumentUserDto;
import com.globits.letter.service.ViewDocumentUserService;

@RestController
@RequestMapping("/api/letter")
public class RestViewDocumentController {
	@Autowired
	ViewDocumentUserService service;
	
	@RequestMapping(value = "/add/view/{userId}/{documentId}", method = RequestMethod.POST)
	public ViewDocumentUserDto addView(@PathVariable Long userId, @PathVariable Long documentId) {
		return service.addView(userId, documentId);
	}
	
	@RequestMapping(value="/search/view/{documentId}/{pageIndex}/{pageSize}",method = RequestMethod.POST)
	public Page<ViewDocumentUserDto> searchUser(@PathVariable int pageIndex,@PathVariable int pageSize,@PathVariable Long documentId,@RequestBody SearchDocumentDto searchDocumentDto){
		return service.searchUser(searchDocumentDto, documentId, pageIndex, pageSize);
	}
}
