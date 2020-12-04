package com.globits.letter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.globits.core.service.FileDescriptionService;
import com.globits.letter.LetterConstant;
import com.globits.letter.dto.LetterDocumentDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.service.LetterDocumentService;

@RestController
@RequestMapping("/api/letter/document")
public class RestLetterDocumentController {
	@Autowired
	LetterDocumentService service;
	@Autowired
	private FileDescriptionService fileService;
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN,LetterConstant.ROLE_LETTER_USER})
	@RequestMapping(path="/getbypage/{pageSize}/{pageIndex}",method = RequestMethod.GET)
	public Page<LetterDocumentDto> findByPage( @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
		return service.getListDocument(pageIndex, pageSize);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN,LetterConstant.ROLE_LETTER_USER})
	@RequestMapping(path="/{documentId}",method = RequestMethod.GET)
	public LetterDocumentDto findById( @PathVariable Long documentId) {
		return service.getByDocumentById(documentId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN,LetterConstant.ROLE_LETTER_USER})
	@RequestMapping(value="/search/{pageIndex}/{pageSize}",method = RequestMethod.POST)
	public Page<LetterDocumentDto> searchDocument(@PathVariable int pageIndex,@PathVariable int pageSize,@RequestBody SearchDocumentDto searchDto){
		return service.searchDocument(searchDto, pageIndex, pageSize);
	}
}
